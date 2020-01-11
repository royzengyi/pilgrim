package com.royzeng.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AsmPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.extensions.add(Setting.PACAKAGE_CONFIG, PacakageConfig)
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new AsmTransform(project))

    }
}