# ЭПСЭ — выдержка из кода коммерческого проекта

Витрина для чтения: отобранные фрагменты из платформы судебной строительно-технической экспертизы (дела, осмотры с фотофиксацией, дефекты, ведомость объёмов работ, сметы, генерация DOCX-отчётов).

Опубликовано с согласия заказчика. Это **не** полный проект: фрагменты не собираются в работающее приложение — часть импортов ссылается на модули полного репозитория. Полный код доступен для просмотра на собеседовании вживую.

## Живое демо

- Приложение: http://77.110.115.239 (логин `demo` / `demo`)
- API + Swagger: http://77.110.115.239:8080/swagger-ui.html

## Стек

**Backend** — Kotlin 2.1, Spring Boot 3.4, Spring Security (JWT, stateless), Spring Data JPA, PostgreSQL, MinIO/S3, Apache POI.

**Frontend** — Vue 3 (Composition API), TypeScript, Quasar 2, Pinia, axios.

## Что здесь и зачем смотреть

### `backend/auth` + `backend/security` — аутентификация и авторизация

Stateless JWT с парой access/refresh и ротацией refresh-токенов ([BaseAuthService.kt](backend/auth/BaseAuthService.kt)). Гранулярные permissions через собственный `PermissionEvaluator`, подключённый к method security ([AppPermissionEvaluator.kt](backend/security/AppPermissionEvaluator.kt), [SecurityConfig.kt](backend/security/SecurityConfig.kt)). Секреты и время жизни токенов — только через конфигурацию с валидацией ([JwtProperties.kt](backend/security/JwtProperties.kt)).

### `backend/estimate` — сметный отчёт и генерация DOCX

Сбор данных сметы по локациям и видам работ ([EstimateReportBuilder.kt](backend/estimate/EstimateReportBuilder.kt)) и генерация готового DOCX-документа через Apache POI: альбомная ориентация, таблицы с объединением ячеек, группировки и итоги ([docx/EstimateReportDocxBuilder.kt](backend/estimate/docx/EstimateReportDocxBuilder.kt)).

### `backend/s3` — файловое хранилище

Загрузка и выдача файлов через presigned URL: бэкенд не проксирует тяжёлый трафик, клиент ходит в S3 напрямую по временной ссылке ([S3Service.kt](backend/s3/S3Service.kt)).

### `backend/defect` — эталонный доменный модуль

Организация бэкенда по доменам, а не по слоям: entity → repository → service → controller → DTO в одном пакете. В `search/` — динамические фильтры на Spring Data JPA Specifications ([search/SearchSpec.kt](backend/defect/search/SearchSpec.kt)).

### `backend/exceptions` — обработка ошибок

Единый `@RestControllerAdvice` с типизированным ответом об ошибке ([GlobalExceptionHandler.kt](backend/exceptions/GlobalExceptionHandler.kt)).

### `frontend/boot/axios.ts` — HTTP-слой

Refresh-token flow: перехват 401, обновление токена, очередь накопившихся запросов с повтором после обновления, защита от параллельных refresh.

### `frontend/features/boq` — feature-sliced модуль

Полный фича-модуль ведомости объёмов работ: `api` / `components` / `composables` / `stores`. Так устроены все 18 фич проекта.

## Права

Код опубликован для демонстрации в портфолио с согласия заказчика. Все права защищены; использование и распространение — только с согласия автора.
