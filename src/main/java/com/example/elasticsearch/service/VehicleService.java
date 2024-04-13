package com.example.elasticsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.elasticsearch.document.Vehicle;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.search.SearchRequestDTO;
import com.example.elasticsearch.search.util.SearchUtil;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class VehicleService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final RestHighLevelClient client;

    @Autowired
    public VehicleService(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * 用於根據 {@link SearchRequestDTO} DTO 中提供的資料搜尋車輛。 欲了解更多信息，請查看
     * 在 DTO javadoc 中。
     *
     * @param dto DTO 包含有關搜尋內容的資訊。
     * @return 傳回找到的車輛清單。
     */
    public List<Vehicle> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                dto
        );

        return searchInternal(request);
    }

    /**
     用於取得自指定日期以來創建的所有車輛。
     @param date 轉送到搜尋的日期。
     @return 返回自轉發日期以來創建的所有車輛。
     */
    public List<Vehicle> getAllVehiclesCreatedSince(final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                "created",
                date
        );

        return searchInternal(request);
    }

    /**
     * 搜尋自指定日期以來創建的車輛。
     *
     * @param dto 搜尋請求的 DTO 物件，包含搜尋條件等資訊
     * @param date 指定的日期，用於搜尋自該日期以來創建的車輛
     * @return 返回自指定日期以來創建的符合搜尋條件的車輛列表
     */
    public List<Vehicle> searchCreatedSince(final SearchRequestDTO dto, final Date date) {
        // 構建搜尋請求物件
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX, // 索引名稱
                dto, // 搜尋請求的 DTO 物件，包含搜尋條件等資訊
                date // 指定的日期
        );

        // 調用內部搜尋方法執行搜尋並返回結果
        return searchInternal(request);
    }

    /**
     * 在 Elasticsearch 中進行內部搜尋。
     *
     * @param request 搜尋請求
     * @return 返回符合搜尋條件的車輛列表，如果發生異常則返回空列表
     */
    private List<Vehicle> searchInternal(final SearchRequest request) {
        // 如果搜尋請求為空，則記錄錯誤並返回空列表
        if (request == null) {
            LOG.error("無法建立搜尋請求");
            return Collections.emptyList();
        }

        try {
            // 發送搜尋請求並獲取回應
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            // 獲取搜尋結果中的搜尋命中（SearchHit）數組
            final SearchHit[] searchHits = response.getHits().getHits();
            // 創建一個列表來存儲車輛物件
            final List<Vehicle> vehicles = new ArrayList<>(searchHits.length);
            // 將每個搜尋命中轉換為車輛物件並添加到列表中
            for (SearchHit hit : searchHits) {
                vehicles.add(
                        MAPPER.readValue(hit.getSourceAsString(), Vehicle.class)
                );
            }

            return vehicles; // 返回車輛列表
        } catch (Exception e) {
            LOG.error(e.getMessage(), e); // 記錄異常信息
            return Collections.emptyList(); // 返回空列表表示發生異常
        }
    }

    /**
     * 將車輛資訊索引到 Elasticsearch 中。
     *
     * @param vehicle 要索引的車輛資訊
     * @return 如果索引成功，則返回true；否則返回false
     */
    public Boolean index(final Vehicle vehicle) {
        try {
            // 將車輛物件轉換為 JSON 字串
            final String vehicleAsString = MAPPER.writeValueAsString(vehicle);

            // 創建一個索引請求物件，並指定要索引的索引名稱
            final IndexRequest request = new IndexRequest(Indices.VEHICLE_INDEX);
            // 設置文件的ID
            request.id(vehicle.getId());
            // 設置文件內容
            request.source(vehicleAsString, XContentType.JSON);

            // 發送索引請求，並獲取回應
            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            // 檢查回應是否成功，並返回相應的結果
            return response != null && response.status().equals(RestStatus.OK);
        } catch (final Exception e) {
            // 記錄異常資訊
            LOG.error(e.getMessage(), e);
            return false; // 返回false表示索引過程中出現異常
        }
    }

    public Vehicle getById(final String vehicleId) {
        try {
            // 傳送請求以取得指定ID的文件訊息
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.VEHICLE_INDEX, vehicleId), // 建立一個取得指定索引和ID的GetRequest對象
                    RequestOptions.DEFAULT // 使用預設的請求選項
            );

            // 檢查所取得的文件資訊是否為空或來源文件是否為空
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null; // 如果為空，則傳回null
            }

            // 將來源文件轉換為字串，然後使用物件映射器（ObjectMapper）將其轉換為Vehicle對象
            return MAPPER.readValue(documentFields.getSourceAsString(), Vehicle.class);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e); // 記錄異常訊息
            return null; // 回傳null表示出現異常
        }
    }
}
