package app.revanced.patches.youtube.video.voiceovertranslation

import app.revanced.patches.youtube.misc.extension.sharedExtensionPatch
import app.revanced.patches.youtube.video.information.videoInformationPatch
import app.revanced.patches.youtube.video.information.videoTimeHook
import app.revanced.patches.youtube.video.videoid.hookVideoId
import app.revanced.patches.youtube.video.videoid.videoIdPatch
import app.revanced.patches.youtube.misc.playertype.playerTypeHookPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/revanced/extension/youtube/patches/VoiceOverTranslationPatch;"

/**
 * Патч Voice Over Translation
 * 
 * Отслеживает изменение videoId и управляет воспроизведением аудио перевода
 * параллельно с видео.
 * 
 * Использует существующие хуки ReVanced:
 * - hookVideoId для отслеживания изменения videoId
 * - videoTimeHook для отслеживания времени воспроизведения
 * - VideoState для отслеживания состояния воспроизведения
 * - VideoInformation для получения информации о видео
 */
val voiceOverTranslationPatch = bytecodePatch(
    name = "Voice Over Translation",
    description = "Добавляет поддержку голосового перевода для YouTube видео. " +
            "При изменении videoId загружает перевод с сервера и воспроизводит его параллельно с видео."
) {
    dependsOn(
        sharedExtensionPatch,
        videoIdPatch,
        videoInformationPatch,
        playerTypeHookPatch,
    )

    // Поддерживаем только последнюю версию YouTube
    // Версию нужно обновлять при каждом обновлении YouTube
    // Проверяйте актуальную версию в ReVanced Manager или на GitHub
    compatibleWith("com.google.android.youtube")

    // Добавляем extension (DEX файл с логикой)
    extendWith("extensions/youtube.rve")

    execute {
        // Хук для отслеживания изменения videoId
        // Вызывается когда загружается новое видео
        hookVideoId("$EXTENSION_CLASS_DESCRIPTOR->onVideoIdChanged(Ljava/lang/String;)V")

        // Хук для отслеживания времени воспроизведения
        // Вызывается примерно раз в секунду с текущей позицией видео
        videoTimeHook(EXTENSION_CLASS_DESCRIPTOR, "onVideoTimeChanged")
    }
}
