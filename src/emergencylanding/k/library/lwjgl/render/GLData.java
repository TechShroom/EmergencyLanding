package emergencylanding.k.library.lwjgl.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import k.core.util.classes.StackTraceInfo;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import emergencylanding.k.library.util.LUtils;

/** All OpenGL will be handled here */
public class GLData {
    /**
     * Indexes for BindAttribLocation
     */
    public static int POSITION_INDEX = 0, COLOR_INDEX = 1, TEX_INDEX = 2;
    private static final Matrix4f identity_matrix = Matrix4f
            .setIdentity(new Matrix4f());
    private static Matrix4f orthoMatrix = null;
    private static FloatBuffer orthoMatrixData = null;
    private static int comboShaderProgram = 0;
    private static ArrayList<Integer> shaders = new ArrayList<Integer>();
    private static int orthoMatrixLocation = 0;
    public static int uniformTexEnabler = 0;

    public static void clearAndLoad() {

        GL20.glUseProgram(comboShaderProgram);
        GL20.glUniformMatrix4(orthoMatrixLocation, false, orthoMatrixData);
        GL20.glUseProgram(NONE);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL20.glUseProgram(comboShaderProgram);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void unload() {
        GL20.glUseProgram(NONE);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void initOpenGL() {
        GLData.resizedRefresh();
        init();
        addVertexAndFragmentShaders();
        runOrthoCalcs();
        comboShaders();
        getLocations();
        RenderManager.registerRenders();
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void init() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void closeOpenGL() {
        unbindComboShader();
        detachShaders();
        removeShaders();
        destroyComboShader();
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void resizedRefresh() {
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void comboShaders() {
        comboShaderProgram = GL20.glCreateProgram();
        for (int shader : shaders) {
            GL20.glAttachShader(comboShaderProgram, shader);
        }
        GL20.glBindAttribLocation(comboShaderProgram, POSITION_INDEX,
                "in_Position");
        GL20.glBindAttribLocation(comboShaderProgram, COLOR_INDEX, "in_Color");
        GL20.glBindAttribLocation(comboShaderProgram, TEX_INDEX, "in_texCoord");
        GL20.glLinkProgram(comboShaderProgram);
        GL20.glValidateProgram(comboShaderProgram);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void addVertexAndFragmentShaders() {
        shaders.add(loadShader(
                LUtils.getELTop()
                        + "/res/shaders/vertex.glsl".replace('/',
                                File.separatorChar), GL20.GL_VERTEX_SHADER));
        shaders.add(loadShader(
                LUtils.getELTop()
                        + "/res/shaders/texture.glsl".replace('/',
                                File.separatorChar), GL20.GL_FRAGMENT_SHADER));
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void runOrthoCalcs() {
        int right = Display.getWidth();
        int left = 0;
        int top = Display.getHeight();
        int bottom = 0;
        int far = -100;
        int near = 100;
        orthoMatrix = new Matrix4f(identity_matrix);
        orthoMatrix.m00 = 2.0f / (right - left);

        orthoMatrix.m11 = 2.0f / (top - bottom);

        orthoMatrix.m22 = -2.0f / (far - near);

        orthoMatrix.m30 = -(right + left) / (right - left);
        orthoMatrix.m31 = -(top + bottom) / (top - bottom);
        orthoMatrix.m32 = -(far + near) / (far - near);
        orthoMatrixData = BufferUtils.createFloatBuffer(16);
        orthoMatrix.store(orthoMatrixData);
        orthoMatrixData.flip();
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void getLocations() {
        orthoMatrixLocation = GL20.glGetUniformLocation(comboShaderProgram,
                "orthoMatrix");
        uniformTexEnabler = GL20.glGetUniformLocation(comboShaderProgram,
                "utexenabled");
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            LUtils.print("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) {
            int len = GL20.glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH);
            ByteBuffer chars = BufferUtils.createByteBuffer(len);
            if (chars.capacity() == 0) {
                throw new IllegalStateException(
                        "No errors, shaders broken SEVERELY");
            }
            System.err.println(GL20.glGetShaderInfoLog(shaderID, len));
        }
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        return shaderID;
    }

    private static void unbindComboShader() {
        GL20.glUseProgram(NONE);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void detachShaders() {
        for (int shader : shaders) {
            GL20.glDetachShader(comboShaderProgram, shader);
        }
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void removeShaders() {
        for (int shader : shaders) {
            GL20.glDeleteShader(shader);
        }
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void destroyComboShader() {
        GL20.glDeleteProgram(comboShaderProgram);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    /**
     * The number that unbinds anything in OpenGL
     */
    public static final int NONE = 0;

    public static void notifyOnGLError(String location) {
        int err = GL11.glGetError();
        if (err != GL11.GL_NO_ERROR) {
            LUtils.print("[GLErrorReporter] GLError in " + location + ": "
                    + GLU.gluErrorString(err) + " (id: " + err + ")");
            System.exit(-10);
        }
    }
}
