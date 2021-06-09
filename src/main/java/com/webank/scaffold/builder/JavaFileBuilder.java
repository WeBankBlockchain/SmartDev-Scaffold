package com.webank.scaffold.builder;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaFileBuilder {

    String getJavaFilePackage(String relativePackage);

    List<TypeSpec> buildTypeSpec(String fullPkg);

    default void generateJavaFile(String relativePackage, File rootFile) throws IOException {
        String pkg = getJavaFilePackage(relativePackage);
        List<TypeSpec> typeSpecs = buildTypeSpec(pkg);
        if(typeSpecs == null) return;
        for (TypeSpec typeSpec: typeSpecs) {
            if(typeSpec != null){
                JavaFile file = JavaFile.builder(pkg, typeSpec).build();
                file.writeTo(rootFile);
            }
        }
    }
}
