package ru.hse.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.cli.command.Status.ERROR;
import static ru.hse.cli.command.Status.EXIT;
import static ru.hse.cli.command.Status.OK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.hse.cli.command.CdCommand;
import ru.hse.cli.command.LsCommand;
import ru.hse.cli.entities.BoundPipe;
import ru.hse.cli.entities.Result;
import ru.hse.cli.utils.PipeParseUtils;

public class CommandTests {

	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeEach
	public void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
	}

	@Test
	public void EchoTest() {
		String sampleCommandInput = "echo 'hello\nworld'";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(OK, result.status());
		Assertions.assertEquals("'hello world'", outputStreamCaptor.toString().trim());
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
	public void PwdTest() {
		String sampleCommandInput = "pwd";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(OK, result.status());
		Assertions.assertEquals(0, result.value());
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
	public void ExitTest() {
		String sampleCommandInput = "exit";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(EXIT, result.status());
		Assertions.assertEquals(0, result.value());
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
	public void WcTest() {
		String sampleCommandInput = "wc ../cli/src/test/java/ru/hse/cli/test_data.txt";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(OK, result.status());
		Assertions.assertEquals(0, result.value());
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
	public void PipeWcTest() {
		String sampleCommandInput = "echo kek | wc";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(OK, result.status());
		Assertions.assertEquals(0, result.value());
		String[] expectedResults = { "1	1	5", "1	1	4" };
		List<String> expectedResultsList = Arrays.asList(expectedResults);
		Assertions.assertTrue(expectedResultsList.contains(outputStreamCaptor.toString().trim()));
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
	public void CatTest() {
		String sampleCommandInput = "cat ../cli/src/test/java/ru/hse/cli/test_data.txt";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(OK, result.status());
		Assertions.assertEquals(0, result.value());
		Assertions.assertEquals("I want to fit in.", outputStreamCaptor.toString().trim());
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
	public void ExternalCommandTest() {
		String sampleCommandInput = "ls";
		BoundPipe boundPipe = PipeParseUtils.parse(sampleCommandInput);
		Result result = boundPipe.execute();
		Assertions.assertEquals(OK, result.status());
		Assertions.assertEquals(0, result.value());
		Assertions.assertFalse(result.message().isPresent());
	}

	@Test
    public void CdTest() {
        String initialDirectory = System.getProperty("user.dir");

        CdCommand cdCommand = new CdCommand();
        InputStream in = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Result result = cdCommand.invoke(Arrays.asList("cd", "src"), in, out);

        Assertions.assertEquals(OK, result.status());
		String currentDirectory = System.getProperty("user.dir");
        assertEquals("src", currentDirectory.substring(currentDirectory.length() - 3));

        System.setProperty("user.dir", initialDirectory);
    }

    @Test
    public void LsTest() {
        String testFileName = "testFile.txt";
        File testFile = new File(testFileName);
        try {
            testFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LsCommand lsCommand = new LsCommand();
        InputStream in = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Result result = lsCommand.invoke(Arrays.asList("ls"), in, out);

        testFile.delete();

        Assertions.assertEquals(OK, result.status());
        assertTrue(out.toString(StandardCharsets.UTF_8).contains(testFileName));
    }

}
