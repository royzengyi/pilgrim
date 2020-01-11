package com.royzeng.plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;


public class AsmMethodVisitor extends LocalVariablesSorter  implements Opcodes{

    private int startVarIndex;
    private String methodName;

    public AsmMethodVisitor(MethodVisitor methodVisitor, String name, int access, String descriptor) {
        super(ASM7, access, descriptor, methodVisitor);
        methodName = name.replace("/", ".");
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitMethodInsn(INVOKESTATIC, Setting.SYSTEM_CLOCK_PACKAGE, Setting.SYSTEM_CLOCK_METHOD, Setting.SYSTEM_CLOCK_METHOD_SIGNATURE, false);
        startVarIndex = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(Opcodes.LSTORE, startVarIndex);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            mv.visitMethodInsn(INVOKESTATIC, Setting.SYSTEM_CLOCK_PACKAGE, Setting.SYSTEM_CLOCK_METHOD, Setting.SYSTEM_CLOCK_METHOD_SIGNATURE, false);
            mv.visitVarInsn(LLOAD, startVarIndex);
            mv.visitInsn(LSUB);
            int index = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, index);
            mv.visitLdcInsn(methodName);
            mv.visitVarInsn(LLOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, Setting.TIMEDEBUGERMANAGER_CLASS, Setting.TIMEDEBUGERMANAGER_METHOD, Setting.TIMEDEBUGERMANAGER_METHOD_SIGNATURE, false);
        }
        super.visitInsn(opcode);
    }
}

