1.GET /person/_search
這是最簡單的搜尋URL語法，用於執行對索引 person 中所有文件的搜尋。
2.GET /person/_search?q=name:John
這個URL語法中的 q 參數用來指定搜尋條件。 在上述範例中，它將傳回 name 欄位中包含 "John" 的所有文件。
3.GET /person/_search?q=age:>30&size=10
在這個範例中，q 參數指定了搜尋條件（年齡大於30），size 參數指定了要傳回的文件數量為10。
4.定搜尋條件和排序：
GET /person/_search?q=city:New%20York&sort=age:desc
在這個範例中，q 參數指定了搜尋條件（城市為 "New York"），sort 參數指定了依照年齡欄位降序排序。
5.POST
POST /person/_search
{
"query": {
"match": {
"name": "John"
}
}
}
這個範例中使用的是POST請求，搜尋條件透過請求體中的JSON資料來指定。