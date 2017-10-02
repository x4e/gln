package gln.framebuffer


import gln.get
import gln.renderbuffer.renderbufferName
import org.lwjgl.opengl.GL30
import java.nio.IntBuffer
import kotlin.properties.Delegates

/**
 * Created by elect on 18/04/17.
 */

var framebufferName: IntBuffer by Delegates.notNull()


inline fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Enum<*>) = GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbufferName[renderbuffer])
inline fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: IntBuffer) = GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer[0])


inline fun glBindFramebuffer(target: Int, framebuffer: Enum<*>) = GL30.glBindFramebuffer(target, framebufferName[framebuffer])
inline fun glBindFramebuffer(framebuffer: Enum<*>) = GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferName[framebuffer])
inline fun glBindFramebuffer(framebuffer: IntBuffer) = GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer[0])
inline fun glBindFramebuffer(framebuffer: Int) = GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer)
inline fun glBindFramebuffer() = GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)


inline fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int) = GL30.glFramebufferTexture2D(target, attachment, textarget, texture, 0)