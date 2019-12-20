package net.adoptopenjdk.icedteaweb.integration.classloader;

import net.sourceforge.jnlp.JNLPFile;
import net.sourceforge.jnlp.runtime.classloader2.JnlpApplicationClassLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Locale;

import static net.adoptopenjdk.icedteaweb.integration.classloader.ClassloaderTestUtils.JAR_1;
import static net.adoptopenjdk.icedteaweb.integration.classloader.ClassloaderTestUtils.JAR_2;
import static net.adoptopenjdk.icedteaweb.integration.classloader.ClassloaderTestUtils.createFile;

@Execution(ExecutionMode.SAME_THREAD)
public class LocaleSpecificClassloaderIntegrationTests {

    private static Locale defaultLocale;

    @BeforeAll
    public static void init() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.GERMAN);
    }

    @AfterAll
    public static void end() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void testLoadForConcreteLocale() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final JNLPFile file = createFile("integration-app-12.jnlp");

        //when
        new JnlpApplicationClassLoader(file, jarProvider);

        //than
        Assertions.assertEquals(2, jarProvider.getDownloaded().size());
        Assertions.assertTrue(jarProvider.hasTriedToDownload(JAR_1));
        Assertions.assertTrue(jarProvider.hasTriedToDownload(JAR_2));
    }

    @Test
    public void testNotLoadForWrongLocale() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final JNLPFile file = createFile("integration-app-13.jnlp");

        //when
        new JnlpApplicationClassLoader(file, jarProvider);

        //than
        Assertions.assertEquals(1, jarProvider.getDownloaded().size());
        Assertions.assertTrue(jarProvider.hasTriedToDownload(JAR_1));
        Assertions.assertFalse(jarProvider.hasTriedToDownload(JAR_2));
    }


}
