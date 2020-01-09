package com.royzeng.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Set;

class AsmTransform extends Transform {

    private Project mProject;
    private URLClassLoader mClassLoader;
    private PacakageConfig pkgConfig;

    public AsmTransform(Project project) {
        mProject = project;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        pkgConfig =  (PacakageConfig)mProject.getExtensions().getByName(Setting.PACAKAGE_CONFIG);

        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        outputProvider.deleteAll();

        mClassLoader = ClassLoaderHelper.getClassLoader(inputs, transformInvocation.getReferencedInputs(), mProject);

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                transformJar(jarInput.getFile(), dest);
            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);
                transformDir(directoryInput.getFile(), dest);
            }
        }
    }

    @Override
    public String getName() {
        return AsmTransform.class.getSimpleName();
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    private void transformJar(File input, File dest) {
        System.out.println("transformJar input:" + input + " dest:" + dest);
        try {
            FileUtils.copyFile(input, dest);
        } catch (IOException e) {
            System.out.println("transformJar IOException");
            e.printStackTrace();
        }
    }

    private void transformDir(File inputDir, File outputDir) {
        System.out.println("transformDir input:" + inputDir + " dest:" + outputDir);
        final String inputDirPath = inputDir.getAbsolutePath();
        final String outputDirPath = outputDir.getAbsolutePath();
        if (inputDir.isDirectory()) {
            for (final File file : com.android.utils.FileUtils.getAllFiles(inputDir)) {
                    String filePath = file.getAbsolutePath();
                    File outputFile = new File(filePath.replace(inputDirPath, outputDirPath));
                transformSingleFile(file, outputFile);
            }
        }
    }

    private void transformSingleFile(File input, File dest) {
        String inputPath = input.getAbsolutePath();
        String outputPath = dest.getAbsolutePath();

        if (needInject(inputPath)) {
            System.out.println("transformSingleFile input:" + inputPath);
            try {
                FileUtils.touch(dest);
                FileInputStream is = new FileInputStream(inputPath);
                ClassReader cr = new ClassReader(is);
                ClassWriter cw = new AsmClassWriter(mClassLoader, ClassWriter.COMPUTE_FRAMES);
                MethodClassAdapter adapter = new MethodClassAdapter(cw);
                cr.accept(adapter,  ClassReader.EXPAND_FRAMES);
                FileOutputStream fos = new FileOutputStream(outputPath);
                fos.write(cw.toByteArray());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("copySingleFile input:" + inputPath);
            try {
                FileUtils.copyFile(input, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean needInject(String filePath) {
        if(pkgConfig != null && !pkgConfig.pkgList.isEmpty()) {
            for(String item : pkgConfig.pkgList) {
                if(filePath.contains(item.replace('.', '\\'))) {
                    return true;
                }
            }
        }
        return false;
    }
}