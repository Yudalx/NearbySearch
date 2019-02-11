# Задание 2

# Объект тестирования - google api Метод    /nearbysearch    GET

Endpoint -    https://maps.googleapis.com/maps/api/place

Пример запроса    https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters

Все ответы будем принимать в json

Зависимости:
- java 8
- maven 3.6

Запуск проекта
- из папки проекта в консоли запустить ***mvn site:site***
Мавен скачает и установит все зависимости проекта, запустит тесты и сгенерирует отчет
### Отчет по тестированию будет по адресу ${project.build.directory}/target/site/index.html

# Цель тестирования - проверка работы сервиса для позитивных и негативных сценариев

# Параметры метода nearbysearch
- key (requered) - ключ от апи
- location (requered) - координаты точки принимает два параметра в формате широта -latitude долгота - longitude, пример (55.6372523,37.5203141)
- radius -  (requered if rankby=distance is not specified) - радиус вокруг указанной точки в метрах, принимает числовое значение от 1 до 50000
- keyword - поиск по кодовому слову(принимаются любые совпадения которые google проидексировал для указанного места), пример "sun"
- language - параметр который принимает код языка, на котором если это возможно будут продемонстрированы результаты поискаm, пример "en"
- minprice and maxprice - параметры стоимости места, по которым фильтруется результат, пример "2"
- name (deprecated) - не рекомендуется использовать, на данный момент этот параметр просто добавляется к параметру keyword, пример "bar"
- opennow - флаг по которому можно определить работает ли заведение в данный момент, пример "true"
- rankby - принимает два параметра:
distance - сортировка мест по возрастанию дистанции от указанной точки(нельзя использовать вместе с radius)
prominence - сортировка мест по рейтингу
- type - фильтрует места по указанному типу, пример "establishment"
- pagetoken - токен по которому можно получить следующие 20 результатов поиска

# Позитивные кейсы

1.  Проверка работы сервиса с минимальным набором параметров с использованием radius: location, radius, key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=25000&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством мест в указанном радиусе данной точки

2.  Проверка работы сервиса с минимальным набором параметров с использованием rankby=distance: location, rankby, type, key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&rankby=distance&type=bar&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством баров отсортированных в порядке возрастания расстояния от указанной точки

3.  Максимальные параметры с radius: location, radius, language=en, minprice=0, name, keyword, type, key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&minprice=0&radius=1500&language=en&type=bar&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4&name=The" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством баров в указанном радиусе данной точки, без ограничения по стоимости

4.  Максимальные параметры с rankby=distance и верхней границы maxprice: location, rankby, language=en, maxprice=4, name, keyword, type, key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&maxprice=4&rankby=distance&language=en&type=restaurant&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4&name=The" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством ресторанов отсортированных в порядке возрастания расстояния от указанной точки, без ограничения по стоимости, с совпадением по указанному ключевому слову

5.  Максимальные параметры с rankby=prominence: location, rankby, language=en, radius maxprice=4, name, keyword, type, key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&maxprice=4&rankby=prominence&language=en&radius=2000&type=cafe&keyword=establishment&name=The&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством кафе в указанном радиусе данной точки, без ограничения по стоимости, отсортированных по рейтингу в порядке убывания

6.  Проверка верхней границы параметра radius=50000
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=50000&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством мест в указанном радиусе данной точки

7.  Проверка нижней границы параметра radius=1
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=1&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством мест в указанном радиусе данной точки

8.  Проверка верхней границы параметров широты(latitude)+90 и долготы (longitude) +180
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=90,180&radius=49999&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством мест в указанном радиусе данной точки

9. Проверка нижней границы параметров широты(latitude)-90 и долготы (longitude) -180
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-90,-180&radius=30000&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством мест в указанном радиусе данной точки

10. Проверка верхней границы параметра minprice=4
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&minprice=4&radius=10500&language=en&type=bar&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c количеством мест в указанном радиусе данной точки и price_level=4


# Негативные кейсы

1. Проверка работы сервиса в случае если мест по указанным параметрам не найдено maxprice=0
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&maxprice=0&radius=50000&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=ZERO_RESULTS

2.  Проверка ответа сервиса в случае отсутствия обязательного параметра location
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?&radius=1&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON с пустым массивом results, status INVALID_REQUEST

3.  Проверка ответа сервиса в случае выхода за верхнюю границу параметров location.latitude +91 location.longitude-181
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=91, -181&rankby=distance&type=bar&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

4.  Проверка ответа сервиса в случае выхода за нижнюю границу параметра location.latitude -91 location.longitude +181
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-91, 181&rankby=distance&type=bar&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

5.  Проверка ответа сервиса в случае выхода за верхнюю границу параметра radius = 50001
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=50001&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

6.  Проверка ответа сервиса в случае выхода за нижнюю границу параметра radius = -1
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=-1&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

7. Проверка ответа сервиса в случае выхода за границу параметра minprice=5
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&minprice=5&rankby=prominence&language=en&radius=2000&type=bar&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

8.  Проверка ответа сервиса в случае выхода за нижнюю границу параметра minprice=-1
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&minprice=-1&rankby=prominence&language=en&radius=2000&type=bar&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

9. Проверка ответа сервиса в случае выхода за верхнюю границу параметра maxprice=5
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&maxprice=5&rankby=prominence&language=en&radius=2000&type=bar&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

10. Проверка ответа сервиса в случае выхода за нижнюю границу параметра maxprice=-1
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.7701495,4.8517567&maxprice=-1&rankby=prominence&language=en&radius=2000&type=bar&keyword=establishment&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

11. Проверка ответа сервиса в случае если с параметром rankby=distance не указан ни один из обязательных параметров: keyword, name, type
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&rankby=distance&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

12. Проверка ответа сервиса в случае если в запросе указаны взаимоисключающие параметры rankby=distance и radius
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=25000&rankby=distance&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs4" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON c параметром status=INVALID_REQUEST

# Не автоматизированные
1.  Проверка ответа сервиса в случае не валидного параметра key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=25000&key=AIzaSyCyOG34X2U6iKWgvwGmukiaBaVr3-PkNs1" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON с "error_message = The provided API key is invalid.",
и статус REQUEST_DENIED

2.  Проверка ответа сервиса в случае отсутствия обязательного параметра key
```
curl -s "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.6372523,37.5203141&radius=25000" | jq
```
***Ожидаемый результат:*** ответ 200 ok и JSON с пустым массивом results, status INVALID_REQUEST
