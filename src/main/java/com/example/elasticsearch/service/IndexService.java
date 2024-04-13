package com.example.elasticsearch.service;


import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Util;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class IndexService {
    private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);
    private static final List<String> INDICES = List.of(Indices.VEHICLE_INDEX);
    private final RestHighLevelClient client;

    @Autowired
    public IndexService(RestHighLevelClient client) {
        this.client = client;
    }

    @PostConstruct
    public void tryToCreateIndices() {
        // 呼叫 recreateIndices 方法來建立或重建索引
        recreateIndices(false);
    }

    // 建立或重建索引的方法
    public void recreateIndices(final boolean deleteExisting) {
        // 從靜態檔案載入索引的設定訊息
        final String settings = Util.loadAsString("static/es-settings.json");
        // 如果設定資訊為 null，則記錄錯誤日誌並傳回
        if (settings == null) {
            LOG.error("Failed to load index settings");
            return;
        }
        // 遍歷索引名稱清單中的每個索引
        for (final String indexName : INDICES) {
            try {
                // 檢查索引是否存在
                final boolean indexExists = client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
                // 如果索引存在，並且指定了刪除現有索引的選項
                if (indexExists) {
                    if (!deleteExisting) {
                        continue;
                    }
                    // 刪除現有索引
                    client.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
                }
                // 建立一個 CreateIndexRequest 對象
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                // 設定索引的設定
                createIndexRequest.settings(settings, XContentType.JSON);
                // 載入索引的映射定義
                final String mappings = loadMappings(indexName);
                // 如果映射不為 null，則設定索引的映射
                if (mappings != null) {
                    createIndexRequest.mapping(mappings, XContentType.JSON);
                }
                // 建立索引
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (final Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    // 載入索引的映射定義
    private String loadMappings(String indexName) {
        // 從靜態檔案載入索引的映射定義
        final String mappings = Util.loadAsString("static/mappings/" + indexName + ".json");
        // 如果對應為 null，則記錄錯誤日誌並傳回 null
        if (mappings == null) {
            LOG.error("Failed to load mappings for index with name '{}'", indexName);
            return null;
        }
        // 回傳映射定義
        return mappings;
    }
}