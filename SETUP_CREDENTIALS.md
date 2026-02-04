# Настройка credentials для сборки

Для сборки проекта нужен доступ к GitHub Packages, где находится плагин `app.revanced.patches`.

## Шаг 1: Создание GitHub Personal Access Token

1. Перейдите на https://github.com/settings/tokens
2. Нажмите "Generate new token" → "Generate new token (classic)"
3. Назовите токен (например, "ReVanced Patches Build")
4. Выберите срок действия (рекомендуется "No expiration" или длительный срок)
5. **ВАЖНО**: Выберите scope `read:packages` (достаточно только этого)
6. Нажмите "Generate token"
7. **Скопируйте токен** - он больше не будет показан!

## Шаг 2: Настройка credentials

Создайте или отредактируйте файл `~/.gradle/gradle.properties`:

```properties
gpr.user=Yasich217
gpr.key=ваш_токен_здесь
```

Замените `ваш_токен_здесь` на токен, который вы скопировали на шаге 1.

**Важно**: Используйте `gpr.user` и `gpr.key`, а не `githubPackagesUsername` и `githubPackagesPassword`.

## Шаг 3: Сборка

После настройки credentials выполните:

```bash
cd revanced-patches
./gradlew :patches:buildAndroid --no-daemon
```

Это создаст файл `patches/build/libs/patches-*.rvp` - это и есть файл с патчами, который использует ReVanced Manager.

## Альтернатива: Использование переменных окружения

Вместо файла `gradle.properties` можно использовать переменные окружения:

```bash
export ORG_GRADLE_PROJECT_githubPackagesUsername=Yasich217
export ORG_GRADLE_PROJECT_githubPackagesPassword=ваш_токен
export ANDROID_HOME=$HOME/android-sdk
./gradlew :patches:buildAndroid --no-daemon
```

**Примечание**: Для работы также нужен Android SDK. Установите его через `yay -S android-sdk android-sdk-platform-tools android-sdk-build-tools` и настройте `ANDROID_HOME`.

## Проверка

После успешной сборки проверьте, что файл создан:

```bash
ls -lh patches/build/libs/patches-*.rvp
```
