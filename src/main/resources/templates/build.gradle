buildscript {
    repositories {
        mavenCentral()
        maven { url "http://maven.aliyun.com/nexus/content/groups/public/"}
        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
        mavenLocal()

    }
    dependencies {
        classpath 'com.webank:solc-gradle-plugin:1.0.0-SNAPSHOT'
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.1.RELEASE")
    }
}
plugins {
    id 'org.springframework.boot' version '2.1.1.RELEASE'
}
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'idea'
apply plugin: 'maven'
apply plugin: 'solc-gradle-plugin'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

sourceCompatibility = 1.8
targetCompatibility = 1.8
group = '${generator.group}'
version = '1.0.0-SNAPSHOT'

solc{
    output = 'src/main/resources'
    onlyAbiBin = true
}


repositories {
    mavenLocal()
    mavenCentral()
    maven { url "http://maven.aliyun.com/nexus/content/groups/public/"}
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    maven { url "https://dl.bintray.com/ethereum/maven/" }
    maven { url "https://oss.sonatype.org/service/local/staging/deploy/maven2"}
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    all {
    }
}

dependencies {

    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.slf4j:slf4j-api:1.7.5'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    compile ('org.fisco-bcos.java-sdk:java-sdk:2.7.0'){
        exclude group: 'org.slf4j'
    }
}


sourceSets {
    main {
        java {
            srcDir 'src/main/java'
        }
        resources  {
            srcDir 'src/main/resources'
        }
    }
}
test {
    useJUnitPlatform()
}

bootJar {
    destinationDir file('dist')
    archiveName project.name + '-exec.jar'
    doLast {
        copy {
            from file('src/main/resources')
            into 'dist'
        }
    }
}

clean {
    println "delete ${projectDir}/dist"
    delete "${projectDir}/dist"
}
