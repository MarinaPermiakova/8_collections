import org.junit.Assert
import org.junit.Test


class WallServiceTest {

    private val service = NoteService
    private val privacyView = listOf("all")
    private val privacyComment = listOf("all")

    @Test
    fun createNote() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        Assert.assertEquals(0, noteId)
    }

    @Test
    fun createComment() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val commentId = service.createComment(noteId, 12, 12, "Комментарий к заметке 1", "11")
        Assert.assertEquals(0, commentId)
    }

    @Test
    fun deleteComment() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val commentId = service.createComment(noteId, 12, 12, "Комментарий к заметке 1", "11")
        Assert.assertEquals(1, service.deleteComment(noteId, commentId))
    }

    @Test
    fun restoreComment() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val commentId = service.createComment(noteId, 12, 12, "Комментарий к заметке 1", "11")
        val checkDeletion = service.restoreComment(noteId, commentId)
        Assert.assertEquals(0, checkDeletion)
    }

    @Test
    fun editNote() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val edited = service.edit(noteId, "Заметка 1", "Новый текст заметки 1", privacyView, privacyComment)
        Assert.assertEquals(1, edited)
    }

    @Test
    fun editComment() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val commentId = service.createComment(noteId, 12, 12, "Комментарий к заметке 1", "11")
        val edited = service.editComment(commentId, 12, "Новый текст комментария к заметке 1")
        Assert.assertEquals(1, edited)
    }

    @Test
    fun deleteNote() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val checkDeletion = service.delete(noteId)
        Assert.assertEquals(1, checkDeletion)
    }

    @Test
    fun getNoteById() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        val note = service.getById(noteId, 0)
        if (note != null) Assert.assertEquals("Текст заметки 1", note.text)
    }

    @Test
    fun getComments() {
        val noteId = service.add("Заметка 1", "Текст заметки 1", privacyView, privacyComment)
        service.createComment(noteId, 3, 5, "Комментарий 1 к заметке 1", "12")
        service.createComment(noteId, 3, 5, "Комментарий 2 к заметке 1", "11")
        val listOfComments = service.getComments(noteId, 3, 1, 2)
        Assert.assertEquals(2, listOfComments.size)
    }
}