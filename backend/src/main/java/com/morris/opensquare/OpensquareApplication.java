package com.morris.opensquare;

import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.utils.PythonScriptEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class OpensquareApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(OpensquareApplication.class, args);
		PythonScriptEngine pythonScriptEngine = applicationContext.getBean(PythonScriptEngine.class);

		// example of using PythonScriptEngine and receiving youtube video transcript
		List<YouTubeTranscribeSegment> output = pythonScriptEngine.processPythonTranscribeScript(
				"transcribe_youtube_video.py",
				"https://www.youtube.com/watch?v=l9AzO1FMgM8"
		);
		output.forEach(segment -> System.out.println("time: " + segment.getTime() + ", text: " + segment.getText()));
	}
}
