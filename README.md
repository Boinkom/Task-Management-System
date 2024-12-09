Вот обновленный README файл с добавлением информации о Swagger для просмотра доступных API функций:

---

# Task Management System

Task Management System — это приложение для управления задачами, разработанное с использованием современных технологий. Этот проект поддерживает развертывание с помощью Docker Compose для упрощения локальной разработки.

## 📋 Требования

Перед началом убедитесь, что на вашем компьютере установлены следующие инструменты:

- [Docker](https://www.docker.com/get-started) (версия 20.10 или выше)
- [Docker Compose](https://docs.docker.com/compose/install/) (версия 2.0 или выше)

## 🚀 Инструкция по запуску

### 1. Клонирование репозитория

Клонируйте проект на ваш локальный компьютер:

```bash
git clone https://github.com/Boinkom/Task-Management-System.git
cd Task-Management-System
```

### 2. Настройка переменных окружения

Убедитесь, что в корневой директории проекта есть файл `.env`. Если его нет, создайте его и настройте необходимые переменные окружения. Пример:

```
DB_HOST=task-db
DB_PORT=5432
DB_NAME=task_management
DB_USER=postgres
DB_PASSWORD=12345678
APP_PORT=8080
```

### 3. Настройка Docker Compose

Убедитесь, что в проекте присутствует файл `docker-compose.yml` с таким содержимым:

```yaml
version: '3.9'

services:
  db:
    image: postgres:alpine
    container_name: postgres_db
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 12345678
      POSTGRES_DB: task_management
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5434:5432"
    networks:
      - app_network

  app:
    container_name: java_app
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: 'jdbc:postgresql://postgres_db:5432/task_management'
    depends_on:
      - db
    networks:
      - app_network

networks:
  app_network:

volumes:
  postgres_data:
```

### 4. Запуск Docker Compose

Для поднятия дев-среды выполните следующую команду:

```bash
docker-compose up --build
```

Docker Compose автоматически соберет образы и запустит все необходимые контейнеры (например, базу данных и бэкенд).

### 5. Доступ к приложению

После успешного запуска:

- Веб-приложение будет доступно по адресу: [http://localhost:8080](http://localhost:8080)
- База данных будет доступна по адресу: [localhost:5434](localhost:5434)
- Для просмотра доступных API функций используйте Swagger по адресу: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 6. Остановка приложения

Чтобы остановить приложение, выполните:

```bash
docker-compose down
```

Это завершит работу всех контейнеров.

## 🛠️ Команды для разработки

- Перезапуск контейнеров после внесения изменений:

```bash
docker-compose up --build
```

- Проверка состояния контейнеров:

```bash
docker ps
```

- Просмотр логов:

```bash
docker-compose logs -f
```

## 📖 Полезные ссылки

- [Docker документация](https://docs.docker.com/)
- [Репозиторий проекта](https://github.com/Boinkom/Task-Management-System)

---
