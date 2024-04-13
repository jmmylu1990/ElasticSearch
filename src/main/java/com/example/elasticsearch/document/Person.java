package com.example.elasticsearch.document;

import com.example.elasticsearch.helper.Indices;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = Indices.PERSON_INDEX)
@Setting(settingPath = "static/es.settings.json")
public @Data class Person {
    /**
     * @Id: 這個標記表示這個欄位是物件的唯一識別符，對應到Elasticsearch索引中的文檔ID。在這個程式碼中，id屬性會被映射到Elasticsearch文檔的ID欄位。
     * Field(type= FieldType.Keyword): 這個標記指定了該欄位在Elasticsearch索引中的型別。在這裡，id屬性被聲明為FieldType.Keyword，這表示它會被當作關鍵字進行處理，而不會進行分詞。
     * @Field(type= FieldType.Text): 這個標記也是指定了該欄位在Elasticsearch索引中的型別。在這裡，name屬性被聲明為FieldType.Text，這表示它會被當作文本進行處理，並且會進行分詞。
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text)
    private String name;
}
