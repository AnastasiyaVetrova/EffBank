# Система управления банковскими картами

## 📝 Описание

Приложения для управления банковскими картами пользователя.
Сервис прослушивает порт 8443 (HTTPS).
Возможности:

- Создание и управление картами
- Просмотр карт
- Переводы между своими картами

---

## 🚀 Быстрый старт с Docker

```bash
git clone https://github.com/AnastasiyaVetrova/EffBank.git
```

> Либо скачайте `.zip` архив с GitHub и распакуйте его вручную.

```bash
docker-compose up --build
```

Это создаст и запустит два контейнера:

- `bank_rest-main-app` — Spring Boot-приложение
- `bank-db` — PostgreSQL база данных

После запуска приложение будет доступно по адресу:

```
https://localhost:8443
```

---

## 🛠 Состав проекта

### `Dockerfile`

- Сначала собирает jar-файл из Maven-проекта
- Затем запускает его в минимальном образе OpenJDK 17 (Alpine)

### `docker-compose.yml`

- Запускает два сервиса: `bank_rest-main` и `bank-db`
- Связывает их в общей сети `bank-network`
- Хранит данные PostgreSQL в volume `postgres_data`

### `Дополнительно`

- В проекте есть файл `.env` с демонстрационными переменными среды
- В проекте есть `demoKeystore`  (хранилище ключей)

---

## 🧩 Первичная инициализация (обязательная)

После первого запуска необходимо выполнить первичную настройку, а именно зарегистрировать первого администратора

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "name": "Алексей",
  "surname": "Иванов",
  "phone": "+79998887766",
  "password": "Pass1234"
}
```

👑 Назначьте роль администратора

```http
PATCH /api/v1/users/first-admin
Authorization: Bearer <ваш JWT токен>
```

---

## 📚 Документация API

Интерактивная документация доступна по адресу:

https://localhost:8443/swagger-ui/index.html

OpenAPI спецификация:

- [JSON](https://localhost:8443/v3/api-docs)
- [YAML](https://localhost:8443/v3/api-docs.yaml)

---

## 📥 Основные эндпоинты

## ✅ Авторизация

Большинство запросов требуют заголовок:
Authorization: Bearer <jwt-token>

### 🔐 Аутентификация

- `POST /api/v1/auth/register` — регистрация пользователя
- `POST /api/v1/auth/login` — получение JWT токена

### 💳 Карты

- `GET /api/v1/cards` — получение списка карт текущего пользователя
- `GET /api/v1/cards/all` — все карты (админ)
- `GET /api/v1/cards/{cardId}/balance` — баланс карты
- `POST /api/v1/cards` — создание новой банковской карты
- `POST /api/v1/cards/transfer` — перевод средств между картами пользователя
- `PATCH /api/v1/cards/status` — изменить статус карты
- `DELETE /api/v1/cards/{cardId}/users/{userId}` — удалить карту пользователя

### 🔒 Блокировка карт

- `POST /api/v1/card-lock/request` — запрос на блокировку/разблокировку карты
- `POST /api/v1/card-lock/order` — получить запрос на обработку
- `POST /api/v1/card-lock/response` — завершение заказа на запрос блокировки

### 👤 Пользователи

- `PATCH /api/v1/users` — обновить данные пользователя
- `PATCH /api/v1/users/first-admin` — назначить первого администратора
- `PATCH /api/v1/users/{userId}/role/{role}` — изменение роли пользователя
- `DELETE /api/v1/users/{userId}/role/{role}` — удалить роль у пользователя

---

## 🔧 Стек технологий

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Liquibase
- Docker
- JWT (аутентификация)
- OpenAPI / Swagger (документация)

---

## 📩 Контакты

Автор: Ветрова Анастасия  
Email: Vetrova600@gmail.com
