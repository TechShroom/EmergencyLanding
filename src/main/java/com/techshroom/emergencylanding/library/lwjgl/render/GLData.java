/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.emergencylanding.library.lwjgl.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.OpenGLException;

import com.flowpowered.math.matrix.Matrix4f;
import com.techshroom.emergencylanding.library.util.LUtils;
import com.techshroom.emergencylanding.library.util.Maths;
import com.techshroom.emergencylanding.library.util.StackTraceInfo;

/** All OpenGL will be handled here */
public class GLData {

    /**
     * Indexes for BindAttribLocation
     */
    public static int POSITION_INDEX = 0, COLOR_INDEX = 1, TEX_INDEX = 2;
    private static Matrix4f orthoMatrix = null;
    private static FloatBuffer orthoMatrixData = null;
    private static int comboShaderProgram = 0;
    private static ArrayList<Integer> shaders = new ArrayList<Integer>();
    private static int orthoMatrixLocation = 0;
    public static int uniformTexEnabler = 0;

    public static void clearAndLoad() {
        notifyOnGLError("clearAndLoad-notOurFault");
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        notifyOnGLError("clearAndLoad-afterClear");
        glUseProgram(comboShaderProgram);
        notifyOnGLError("clearAndLoad-afterUseProgram");
        apply(orthoMatrix);
        notifyOnGLError("clearAndLoad-afterApply");
    }

    public static void unload() {
        // glUseProgram(NONE); // generates INVALID OPERATION, may be needed
        // however?
        try {
            notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        } catch (OpenGLException ogle) {
        }
    }

    public static void initOpenGL(long window) {
        GLData.resizedRefresh(window);
        init();
        addVertexAndFragmentShaders();
        comboShaders();
        getLocations();
        RenderManager.registerRenders();
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void closeOpenGL() {
        unbindComboShader();
        detachShaders();
        removeShaders();
        destroyComboShader();
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static void resizedRefresh(long window) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, width, height);
        runOrthoCalcs(window);
        glViewport(0, 0, width.get(0), height.get(0));
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void comboShaders() {
        comboShaderProgram = glCreateProgram();
        for (int shader : shaders) {
            glAttachShader(comboShaderProgram, shader);
        }
        glBindAttribLocation(comboShaderProgram, POSITION_INDEX, "in_Position");
        glBindAttribLocation(comboShaderProgram, COLOR_INDEX, "in_Color");
        glBindAttribLocation(comboShaderProgram, TEX_INDEX, "in_texCoord");
        glLinkProgram(comboShaderProgram);
        glValidateProgram(comboShaderProgram);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void addVertexAndFragmentShaders() {
        shaders.add(loadShader(LUtils.getELTop()
                + "/shaders/vertex.glsl".replace('/', File.separatorChar),
                GL_VERTEX_SHADER));
        shaders.add(loadShader(LUtils.getELTop()
                + "/shaders/texture.glsl".replace('/', File.separatorChar),
                GL_FRAGMENT_SHADER));
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void runOrthoCalcs(long window) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, width, height);
        int right = width.get(0);
        int left = 0;
        int top = height.get(0);
        int bottom = 0;
        int far = -100;
        int near = 100;
        orthoMatrix = Matrix4f.createOrthographic(right, left, top, bottom,
                near, far);
        storeData();
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void storeData() {
        orthoMatrixData = BufferUtils.createFloatBuffer(16);
        Maths.storeMatrix(orthoMatrix, orthoMatrixData);
        orthoMatrixData.flip();
    }

    public static void getLocations() {
        orthoMatrixLocation =
                glGetUniformLocation(comboShaderProgram, "orthoMatrix");
        uniformTexEnabler =
                glGetUniformLocation(comboShaderProgram, "utexenabled");
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public static int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try (
                BufferedReader reader =
                        new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            LUtils.print("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            if (len == 0) {
                throw new IllegalStateException(
                        "No errors, shaders broken SEVERELY");
            }
            System.err.println(glGetShaderInfoLog(shaderID, len));
        }
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        return shaderID;
    }

    private static void unbindComboShader() {
        glUseProgram(NONE);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void detachShaders() {
        for (int shader : shaders) {
            glDetachShader(comboShaderProgram, shader);
        }
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void removeShaders() {
        for (int shader : shaders) {
            glDeleteShader(shader);
        }
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private static void destroyComboShader() {
        glDeleteProgram(comboShaderProgram);
        notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    /**
     * The number that unbinds anything in OpenGL
     */
    public static final int NONE = 0;

    public static void notifyOnGLError(String location) {
        int err = glGetError();
        if (err != GL_NO_ERROR) {
            LUtils.print("[GLErrorReporter] GLError in " + location + ": "
                    + GLUtil.getErrorString(err) + " (id: " + err + ")");
            throw new OpenGLException(err);
        }
    }

    public static Matrix4f getMatrixToApply() {
        return orthoMatrix;
    }

    public static void apply(Matrix4f m4f) {
        orthoMatrix = m4f;
        storeData();
        glUniformMatrix4fv(orthoMatrixLocation, false, orthoMatrixData);
    }
}
