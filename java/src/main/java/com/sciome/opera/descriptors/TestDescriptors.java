package com.sciome.opera.descriptors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class TestDescriptors
{

	public static void main(String[] args) throws Exception
	{
		ChemicalDescriptorTool cdt = new ChemicalDescriptorTool();

		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(
				Paths.get("/Users/jasonphillips/Downloads/libOPERA2_Java/Sample_50.sdf"),
				StandardCharsets.UTF_8))
		{
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String input = contentBuilder.toString();

		List<OPERAChemicalDescriptors> descriptors = cdt.getDescriptors(input, false, true);

		for (OPERAChemicalDescriptors descriptor : descriptors)
		{

			System.out.println(descriptor.getChemID() + ", " + descriptor.getCDK2Value("nSmallRings"));

			System.out.println(descriptor.getChemID() + ", " + descriptor.getPadelValue("nsmallrings"));
		}

		System.out.println();

	}

}
