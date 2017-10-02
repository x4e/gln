package gln.framebuffer

import gln.buf
import gln.bufAd
import gln.get
import gln.renderbuffer.renderbufferName
import gln.texture.textureName
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL32
import org.lwjgl.system.MemoryUtil.memAddress0
import java.nio.IntBuffer


inline fun initFramebuffer(framebuffer: IntBuffer, block: Framebuffer.() -> Unit) = framebuffer.put(0, initFramebuffer(block))
inline fun initFramebuffer(block: Framebuffer.() -> Unit): Int {
    GL30.nglGenFramebuffers(1, bufAd)
    val res = buf.getInt(0)
    Framebuffer.name = res   // bind
    Framebuffer.block()
    glBindFramebuffer()
    return res
}

inline fun initFramebuffers(block: Framebuffers.() -> Unit) = initFramebuffers(framebufferName, block)
inline fun initFramebuffers(framebuffer: IntBuffer, block: Framebuffers.() -> Unit) {
    GL30.nglGenFramebuffers(framebuffer.capacity(), memAddress0(framebuffer) + framebuffer.position() shl 2)
    Framebuffers.names = framebuffer
    Framebuffers.block()
}

inline fun withFramebuffer(framebuffer: Enum<*>, block: Framebuffer.() -> Unit) = withFramebuffer(framebufferName[framebuffer], block)
inline fun withFramebuffer(framebuffer: IntArray, block: Framebuffer.() -> Unit) = withFramebuffer(framebuffer[0], block)
inline fun withFramebuffer(framebuffer: Int, block: Framebuffer.() -> Unit) {
    Framebuffer.name = framebuffer
    Framebuffer.block()
}

// TODO check if leave
inline fun withFramebuffer(block: Framebuffer.() -> Unit) {
    Framebuffer.name = 0
    Framebuffer.block()
}

object Framebuffer {

    var name = 0
        set(value) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, value)
            field = value
        }

    inline fun texture(attachment: Int, texture: Enum<*>, level: Int = 0) = GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, attachment, textureName[texture], level)
    inline fun texture(attachment: Int, texture: Int, level: Int = 0) = GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, attachment, texture, level)

    inline fun texture2D(target: Int, attachment: Int, texture: Enum<*>, level: Int = 0) = GL30.glFramebufferTexture2D(target, attachment, GL11.GL_TEXTURE_2D, textureName[texture], level)
    inline fun texture2D(target: Int, attachment: Int, texture: Int, level: Int = 0) = GL30.glFramebufferTexture2D(target, attachment, GL11.GL_TEXTURE_2D, texture, level)

    inline fun texture2D(attachment: Int, texture: Enum<*>, level: Int = 0) = GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, textureName[texture], level)
    inline fun texture2D(attachment: Int, texture: Int, level: Int = 0) = GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, texture, level)

    inline fun renderbuffer(attachment: Int, renderbuffer: Enum<*>) = GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, renderbufferName[renderbuffer])
    inline fun renderbuffer(attachment: Int, renderbuffer: IntBuffer) = GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, renderbuffer[0])
    inline fun renderbuffer(attachment: Int, renderbuffer: Int) = GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, renderbuffer)

    val complete: Boolean
        get() {
            val status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)
            return if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
                println("framebuffer incomplete, status: ${when (status) {
                    GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT -> "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT"
                    GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT"
                    GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER -> "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER"
                    GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER -> "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER"
                    GL30.GL_FRAMEBUFFER_UNSUPPORTED -> "GL_FRAMEBUFFER_UNSUPPORTED"
                    GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE -> "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE"
                    GL32.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS -> "GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS"
                    GL30.GL_FRAMEBUFFER_UNDEFINED -> "GL_FRAMEBUFFER_UNDEFINED"
                    else -> throw IllegalStateException()
                }}")
                false
            } else true
        }

    fun getParameter(attachment: Int, pName: Int) = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_FRAMEBUFFER, attachment, pName)

    fun getDepthParameter(pName: Int) = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, pName)
    fun getStencilParameter(pName: Int) = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_FRAMEBUFFER, GL30.GL_STENCIL_ATTACHMENT, pName)
    fun getDepthStencilParameter(pName: Int) = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, pName)

    fun getColorEncoding(index: Int = 0) = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + index, GL30.GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING)
}

object Framebuffers {

    lateinit var names: IntBuffer

    inline fun at(index: Enum<*>, block: Framebuffer.() -> Unit) = at(index.ordinal, block)
    inline fun at(index: Int, block: Framebuffer.() -> Unit) {
        Framebuffer.name = names[index] // bind
        Framebuffer.block()
    }

}