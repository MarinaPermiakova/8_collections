import java.time.ZonedDateTime

object NoteService {
    private var notes = mutableListOf<Note>()
    private var comments = mutableListOf<Comment>()

    // Создает новую заметку у текущего пользователя. После успешного выполнения возвращает идентификатор созданной заметки
    fun add(
            title: String, //  заголовок заметки. строка, обязательный параметр
            text: String, // текст заметки. строка, обязательный параметр
            privacyView: List<String>, // настройки приватности просмотра заметки. список слов, разделенных через запятую, по умолчанию all
            privacyComment: List<String> // настройки приватности комментирования заметки. список слов, разделенных через запятую, по умолчанию all
    ): Long {
        val note = Note(0, 0, title, text, ZonedDateTime.now().toInstant().toEpochMilli(), 0,
                0, "url", privacyView, privacyComment)
        if (notes.isNotEmpty()) note.noteId = notes.last().noteId + 1
        notes.add(note)
        return notes.last().noteId
    }


    // Добавляет новый комментарий к заметке. После успешного выполнения возвращает идентификатор созданного комментария
    fun createComment(
            noteId: Long, // идентификатор заметки. положительное число, обязательный параметр
            ownerId: Long, // идентификатор владельца заметки. положительное число, по умолчанию идентификатор текущего пользователя
            replyTo: Long, // идентификатор пользователя, ответом на комментарий которого является добавляемый комментарий (не передаётся, если комментарий не является ответом). положительное число
            message: String, // текст комментария. строка, обязательный параметр
            guid: String // уникальный идентификатор, предназначенный для предотвращения повторной отправки одинакового комментария. строка
    ): Long {
        val comment = Comment(0, 0, noteId, ownerId, ZonedDateTime.now().toInstant().toEpochMilli(), message, replyTo, guid, 0)
        if (comments.isNotEmpty()) comment.commentId = comments.last().commentId + 1
        comments.add(comment)
        return comments.last().commentId
    }


    // Удаляет заметку текущего пользователя. После успешного выполнения возвращает 1
    fun delete(noteId: Long //  идентификатор заметки. положительное число, обязательный параметр
    ): Int {
        var removed = false
        if (notes.isNotEmpty()) {
            for (note in notes) {
                if (note.noteId == noteId) {
                    removed = notes.remove(note)
                    break
                }
            }
        }
        return if (removed) 1
        else 0
    }


    // Удаляет комментарий к заметке. После успешного выполнения возвращает 1
    fun deleteComment(
            commentId: Long, // идентификатор комментария. положительное число, обязательный параметр
            ownerId: Long // идентификатор владельца заметки. положительное число, по умолчанию идентифи-катор текущего пользователя
    ): Int {
        val removed = 1
        if (comments.isNotEmpty()) {
            for (comment in comments) {
                if ((comment.ownerId == ownerId) && (comment.commentId == commentId)) {
                    comment.removed = removed
                }
            }
        }
        return removed
    }


    // Редактирует заметку текущего пользователя. После успешного выполнения возвращает 1.
    fun edit(
            noteId: Long, // идентификатор заметки. положительное число, обязательный параметр
            title: String, // заголовок заметки. строка, обязательный параметр
            text: String,// текст заметки. строка, обязательный параметр
            privacyView: List<String>, // настройки приватности просмотра заметки. список слов, разделенных через запятую, по умолчанию all, privacy_comment : List<String>
            privacyComment: List<String> // настройки приватности комментирования заметки. список слов, разделенных через запятую, по умолчанию all
    ): Int {
        var edited = false
        if (notes.isNotEmpty()) {
            for (note in notes) {
                if (note.noteId == noteId) {
                    note.title = title
                    note.text = text
                    note.privacyView = privacyView
                    note.privacyComment = privacyComment
                    edited = true
                    break
                }
            }
        }
        return if (edited) 1 else 0
    }


    // Редактирует указанный комментарий у заметки. После успешного выполнения возвращает 1.
    fun editComment(
            commentId: Long,// идентификатор комментария. положительное число, обязательный параметр
            ownerId: Long, // идентификатор владельца заметки. положительное число, по умолчанию идентифи-катор текущего пользователя
            message: String // новый текст комментария.строка
    ): Long {
        var edited = false
        if (comments.isNotEmpty()) {
            for (comment in comments) {
                if ((comment.ownerId == ownerId) && (comment.commentId == commentId)) {
                    comment.message = message
                    edited = true
                    break
                }
            }
        }
        return if (edited) 1 else 0
    }


    // Возвращает список заметок, созданных пользователем.
    fun get(
            noteIds: List<Long>, // идентификаторы заметок, информацию о которых необходимо получить. список положительных чисел, разделенных запятыми
            userId: Long, // идентификатор пользователя, информацию о заметках которого требуется получить. положительное число, по умолчанию идентификатор текущего пользователя
            // offset: Int, // смещение, необходимое для выборки определенного подмножества заметок. положительное число, по умолчанию 0
            count: Int, // количество заметок, информацию о которых необходимо получить. положительное число, по умолчанию 20, максимальное значение 100
            sort: Int // сортировка результатов (0 — по дате создания в порядке убывания, 1 - по дате создания в порядке возрастания). положительное число, по умолчанию 0
    ): List<Note> {
        var listOfNotesByIds: List<Note> = emptyList()
        for ((i, note) in notes.withIndex()) {
            if (note.userId == userId) {
                for (note_id in noteIds) {
                    if (note.noteId == note_id && i < count)
                        listOfNotesByIds = listOfNotesByIds + note
                }
            }
        }
        listOfNotesByIds.sortedBy { sort }
        return listOfNotesByIds
    }


    // Возвращает заметку по её id. После успешного выполнения возвращает список объектов заметок с дополнительными полями:
    //privacy — уровень доступа к заметке (0 — все пользователи, 1 — только друзья, 2 — друзья и друзья дру-зей, 3 — только пользователь);
    //comment_privacy — уровень доступа к комментированию заметки (0 — все пользователи, 1 — только дру-зья, 2 — друзья и друзья друзей, 3 — только пользователь);
    //can_comment — может ли текущий пользователь комментировать заметку (1 — может, 0 — не может).
    fun getById(
            noteId: Long, // идентификатор заметки. положительное число, обязательный параметр
            ownerId: Long, // идентификатор владельца заметки. положительное число, по умолчанию идентификатор текущего пользователя
            // need_wiki: Int // определяет, требуется ли в ответе wiki-представление заметки (работает, только если запрашиваются заметки текущего пользователя). флаг, может принимать значения 1 или 0, по умолчанию 0
    ): Note? {
        var noteById: Note? = null

        for (note in notes) {
            if ((ownerId == note.userId) && (noteId == note.noteId)) {
                noteById = notes[noteId.toInt()]
            }
        }
        return noteById
    }


    // Возвращает список комментариев к заметке - массив объектов comment
    fun getComments(
            noteId: Long, // идентификатор заметки. положительное число, обязательный параметр
            ownerId: Long, // идентификатор владельца заметки. положительное число, по умолчанию идентификатор текущего пользователя
            sort: Int, // сортировка результатов (0 — по дате добавления в порядке возрастания, 1 — по дате добавления в порядке убывания). положительное число, по умолчанию 0
            // offset: Int, // смещение, необходимое для выборки определенного подмножества комментариев. положительное число, по умолчанию 0
            count: Long // количество комментариев, которое необходимо получить. положительное число, по умолча-нию 20, максимальное значение 100
    ): List<Comment> {
        var i = 0
        var listOfComentsByIds: List<Comment> = emptyList()
        comments.forEach { comment ->
            if (comment.noteId == noteId && comment.ownerId == ownerId && i < count) {
                listOfComentsByIds = listOfComentsByIds + comment
                i++
            }
        }
        listOfComentsByIds.sortedBy { sort }
        return listOfComentsByIds
    }


    // Восстанавливает удалённый комментарий.
    fun restoreComment(
            commentId: Long, // идентификатор удаленного комментария. положительное число, обязательный параметр
            ownerId: Long // идентификатор владельца заметки. положительное число, по умолчанию идентификатор текущего пользователя
    ): Int {
        val removed = 0
        if (comments.isNotEmpty()) {
            for (comment in comments) {
                if ((comment.ownerId == ownerId) && (comment.commentId == commentId)) {
                    comment.removed = removed
                }
            }
        }
        return removed
    }
}

