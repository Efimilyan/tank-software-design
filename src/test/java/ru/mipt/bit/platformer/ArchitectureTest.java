package ru.mipt.bit.platformer;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {
    private static final String ROOT = "ru.mipt.bit.platformer";
    private static final String DOMAIN = ROOT + ".model..";
    private static final String APPLICATION = ROOT + ".control..";
    private static final String INPUT = ROOT + ".input..";
    private static final String LEVEL = ROOT + ".level..";
    private static final String GRAPHICS = ROOT + ".graphics..";
    private static final String COMPOSITION_ROOT = ROOT + ".game..";
    private static final String LIBGDX = "com.badlogic..";

    private static final JavaClasses PRODUCTION_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT);

    @Test
    void domainDependsOnNothingOutsideItself() {
        noClasses().that().resideInAPackage(DOMAIN)
                .should().dependOnClassesThat()
                .resideInAnyPackage(APPLICATION, INPUT, LEVEL, GRAPHICS, COMPOSITION_ROOT, LIBGDX)
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void applicationDependsOnlyOnDomain() {
        noClasses().that().resideInAPackage(APPLICATION)
                .should().dependOnClassesThat()
                .resideInAnyPackage(INPUT, LEVEL, GRAPHICS, COMPOSITION_ROOT, LIBGDX)
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void onlyLauncherDependsOnCompositionRoot() {
        noClasses().that().resideOutsideOfPackages(COMPOSITION_ROOT, ROOT)
                .should().dependOnClassesThat().resideInAPackage(COMPOSITION_ROOT)
                .check(PRODUCTION_CLASSES);
    }
    @Test
    void gameLoopDoesNotDependOnGraphicsOrLibGdx() {
        noClasses().that().haveSimpleName("MainCycle")
                .should().dependOnClassesThat().resideInAnyPackage(GRAPHICS, LIBGDX)
                .check(PRODUCTION_CLASSES);
    }
}