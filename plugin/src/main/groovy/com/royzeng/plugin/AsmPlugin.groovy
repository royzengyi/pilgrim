package com.royzeng.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AsmPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        System.out.println("============================")
        System.out.println("自定义Gradle插件开始")
        System.out.println("============================")
    }
}