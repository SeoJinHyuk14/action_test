import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.71"
	kotlin("plugin.spring") version "1.3.71"
	id("io.gitlab.arturbosch.detekt").version("1.7.4")
	id("jacoco")
}

group = "com.kasha"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	compile("org.springframework.boot:spring-boot-starter-web")
	compile("org.springframework.boot:spring-boot-starter-data-jpa")
	compile("com.h2database:h2")
	compile("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(module = "junit")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

detekt {
	toolVersion = "1.7.4"                             // Version of the Detekt CLI that will be used. When unspecified the latest detekt version found will be used. Override to stay on the same version.
	input = files("src/main/java", "src/main/kotlin")    // The directories where detekt looks for source files. Defaults to `files("src/main/java", "src/main/kotlin")`.
	parallel = false                                      // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects. `false` by default.
	config = files("detekt-config.yml")                  // Define the detekt configuration(s) you want to use. Defaults to the default detekt configuration.
	buildUponDefaultConfig = false                        // Interpret config files as updates to the default config. `false` by default.
//	baseline = file("path/to/baseline.xml")               // Specifying a baseline file. All findings stored in this file in subsequent runs of detekt.
	disableDefaultRuleSets = false                        // Disables all default detekt rulesets and will only run detekt with custom rules defined in plugins passed in with `detektPlugins` configuration. `false` by default.
	debug = false                                         // Adds debug output during task execution. `false` by default.
	ignoreFailures = false                                // If set to `true` the build does not fail when the maxIssues count was reached. Defaults to `false`.
	reports {
		xml {
			enabled = true                                // Enable/Disable XML report (default: true)
			destination = file("build/reports/detekt.xml")  // Path where XML report will be stored (default: `build/reports/detekt/detekt.xml`)
		}
		html {
			enabled = true                                // Enable/Disable HTML report (default: true)
			destination = file("build/reports/detekt.html") // Path where HTML report will be stored (default: `build/reports/detekt/detekt.html`)
		}
//		txt {
//			enabled = true                                // Enable/Disable TXT report (default: true)
//			destination = file("build/reports/detekt.txt") // Path where TXT report will be stored (default: `build/reports/detekt/detekt.txt`)
//		}
//		custom {
//			reportId = "CustomJsonReport"                   // The simple class name of your custom report.
//			destination = file("build/reports/detekt.json") // Path where report will be stored
//		}
	}
}

jacoco {
	// JaCoCo 버전
	toolVersion = "0.8.5"

//  테스트결과 리포트를 저장할 경로 변경
//  default는 "${project.reporting.baseDir}/jacoco"
//  reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks.jacocoTestReport {
	reports {
		// 원하는 리포트를 켜고 끌 수 있습니다.
		html.isEnabled = true
		xml.isEnabled = false
		csv.isEnabled = false

//  각 리포트 타입 마다 리포트 저장 경로를 설정할 수 있습니다.
  html.destination = file("$buildDir/jacocoHtml")
  xml.destination = file("$buildDir/jacoco.xml")
	}
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			element = "CLASS"

			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = "0.20".toBigDecimal()	// 최소 커버리지 조절
			}
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
