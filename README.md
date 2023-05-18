### Hexlet tests and linter status:
[![Actions Status](https://github.com/AlexanderKireev/java-project-72/workflows/hexlet-check/badge.svg)](https://github.com/AlexanderKireev/java-project-72/actions)
[![Linter Status](https://github.com/AlexanderKireev/java-project-72/workflows/Build/badge.svg)](https://github.com/AlexanderKireev/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/39e1e1c3751c2b8723d4/maintainability)](https://codeclimate.com/github/AlexanderKireev/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/39e1e1c3751c2b8723d4/test_coverage)](https://codeclimate.com/github/AlexanderKireev/java-project-72/test_coverage)
## Проект "Анализатор страниц" ("Page Analyzer")
Выполнен в рамках обучения в компании "Хекслет" ("Hexlet Ltd.") на курсе java-программирования.

Принят: 5 марта 2023 года. Студент: Киреев Александр. Куратор: Теплинская Мария ("Hexlet Ltd.").

[![Hexlet Ltd. logo](https://raw.githubusercontent.com/Hexlet/assets/master/images/hexlet_logo128.png)](https://ru.hexlet.io/pages/about?utm_source=github&utm_medium=link&utm_campaign=java-package)

Результат Проекта - сайт, анализирующий указанные страницы на SEO пригодность.

Используемые технологии: Фронтенд (Bootstrap, CDN). Фреймворк Javalin (Маршрутизация, Представление). База данных, (ORM Ebean, Миграции, query builders). Деплой (PaaS). HTTP (в том числе выполнение запросов). Интеграционное тестирование. Логгирование.

Веб-адрес развернутого проекта -> https://java-project-72-lq3v.onrender.com/

## Setup
```sh
make build
```
## Migration DB
```sh
make generate-migrations
```

## Run
```sh
make start
```

## Run tests
```sh
make test
```
