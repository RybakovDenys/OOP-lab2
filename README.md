# Лабораторна робота 2: XML, XSD, SAX/DOM/StAX, XSL

Варіант: **Оранжерея** (кореневий елемент `Flowers`, елемент рослини `Flower`).

## Що реалізовано

| Вимога | Де |
|---|---|
| XML-файл | `data/flowers.xml` (8 рослин) |
| XSD-схема: прості й комплексні типи, переліки, граничні значення, обов'язкові атрибути, тип `ID` | `data/flowers.xsd` |
| Java-клас за схемою | `src/main/java/ua/lab2/greenhouse/model` (`Flower`, `VisualParameters`, `GrowingTips`, `Soil`, `Multiplying`) |
| Розбір XML: SAX, DOM, StAX | `src/main/java/ua/lab2/greenhouse/parser` |
| Сортування через `Comparator` | `service/FlowerComparators.java` |
| Валідація за XSD | `service/XmlValidator.java` |
| XSL: розбиття списку на підсписки за типом ґрунту | `data/flowers.xsl`, `service/XslTransformer.java` |
| Уникнення дублювання коду | `FlowerBuilder` (спільний для всіх парсерів), `AbstractFlowerParser` (шаблонний метод), `XmlValued` (спільна логіка переліків) |
| Логування | `java.util.logging`, налаштування у `src/main/resources/logging.properties`, клас `LoggerConfig` |
| Unit-тести | `src/test/java` (JUnit 5) |

## Опис схеми

`Flower` (обов'язковий атрибут `id` типу `xs:ID`, шаблон `flower-N`):

- `Name`, `Origin` - рядки (довжина 2-50);
- `Soil` - перелік: `підзолистий`, `ґрунтовий`, `дерново-підзолистий`;
- `VisualParameters` - `StemColor`, `LeafColor`, `AverageSize` (1-500, атрибут `unit="cm"`);
- `GrowingTips` - `Temperature` (-30..50, `unit="celsius"`), `Photophilous` (`true` - світлолюбна), `Watering` (1-5000, `unit="ml-per-week"`);
- `Multiplying` - перелік: `листям`, `живцями`, `насінням`.

## Збірка та запуск

Потрібні JDK 17+ і Maven 3.8+.

```bash
mvn clean test              # тести + звіт покриття JaCoCo (target/site/jacoco/index.html)
mvn clean package           # збірка target/greenhouse-1.0.0.jar
java -jar target/greenhouse-1.0.0.jar
```

Необов'язкові аргументи: `[xml] [xsd] [xsl] [каталог результатів]`
(за замовчуванням `data/flowers.xml data/flowers.xsd data/flowers.xsl output`).

Програма:

1. валідує XML за XSD;
2. розбирає XML трьома парсерами, сортує колекції за назвою та порівнює результати;
3. виконує XSL-перетворення в `output/flowers_by_soil.xml`.

Повідомлення виводяться через логер (консоль і `logs/greenhouse.log`).

## Приклад перетворення

Початковий XML: усі `Flower` підряд. Після XSL:

```xml
<Flowers>
    <ґрунтовий>
        <Flower id="flower-7">...</Flower>
        <Flower id="flower-2">...</Flower>
    </ґрунтовий>
    <підзолистий>
        <Flower id="flower-5">...</Flower>
    </підзолистий>
</Flowers>
```

## Віддалений репозиторій

```bash
git remote add origin <URL репозиторію>
git push -u origin main
```
