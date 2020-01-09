package com.royzeng.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.util.Arrays;

public class MethodClassAdapter extends ClassVisitor implements Opcodes {

    private boolean isITimeLoggerMethod = false;
    private String className;

    public MethodClassAdapter(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
        isITimeLoggerMethod = Arrays.toString(interfaces).contains(Setting.ITIMELOGGER_CLASS) ||
                className.contains(Setting.TIMEDEBUGERMANAGER_CLASS);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (isITimeLoggerMethod || mv == null) {
            return mv;
        }
        return new AsmMethodVisitor(mv, className + File.separator + name, access, descriptor);
    }
}