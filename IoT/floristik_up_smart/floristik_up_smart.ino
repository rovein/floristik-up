#include <SPI.h>
#include <Ethernet.h>
#include <ArduinoJson.h>
#include "DHT.h"

byte mac[] = {144, 162, 218, 0, 130, 122};
DHT dht(A5, DHT11);
EthernetClient client;
IPAddress server(192, 168, 0, 102);
int storageRoomId = 3;

void setup() {
    Serial.begin(9600);

    while (!Serial) {}

    dht.begin();

    Serial.println("Ініціалізація з'єднання:");

    Ethernet.begin(mac);
    Serial.print(" IP адреса смарт-девайсу ");
    Serial.println(Ethernet.localIP());
   
    delay(1000);
    Serial.print("Під'єднуємося до ");
    Serial.print(server);
    Serial.println("...");

    if (client.connect(server, 8080)) {
        Serial.print("було виконано під'єднання до ");
        Serial.println(client.remoteIP());
    } else {
        Serial.println("зв'язок з сервером не встановлено");
    }
}

void loop() {

    if (client.connect(server, 8080)) {

        float humidity = dht.readHumidity();
        float temperature = dht.readTemperature();
      
        Serial.println(
                "Вологість в приміщенні зберігання квітів: " + String(humidity) + " %\n\r" +
                "Температура в приміщення зберігання квітів: " + String(temperature) + " *C\t");

        const size_t capacity = JSON_OBJECT_SIZE(3);
        DynamicJsonBuffer jsonBuffer(capacity);

        JsonObject &root = jsonBuffer.createObject();
        root["id"] = storageRoomId;
        root["temperature"] = temperature;
        root["humidity"] = humidity;

        String data;
        root.printTo(data);

        client.println("POST /smart-system HTTP/1.1");
        client.println("Host: localhost");
        client.println("User-Agent: Arduino/1.0");
        client.println("Connection: close");
        client.println("Content-Type: application/json");
        client.print("Content-Length: ");
        client.println(data.length());
        client.println();
        client.println(data);

        while (client.connected()) {
          if (client.available()) {
              char c = client.read();
              Serial.print(c);
          }
        }
    }

    delay(100000);
}
