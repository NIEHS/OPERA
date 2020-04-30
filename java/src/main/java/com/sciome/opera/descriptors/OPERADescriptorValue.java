package com.sciome.opera.descriptors;

public class OPERADescriptorValue
{
	String	label;
	String	value;

	public OPERADescriptorValue(String l, String v)
	{
		label = l.replaceAll("\\-", "_");
		value = v;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public boolean equals(Object o)
	{

		if (o == this)
			return true;
		if (!(o instanceof OPERADescriptorValue))
		{
			return false;
		}

		OPERADescriptorValue dv = (OPERADescriptorValue) o;

		return dv.getLabel().equals(getLabel());
	}

	// Idea from effective Java : Item 9
	@Override
	public int hashCode()
	{
		int result = 17;
		result = 31 * result + label.toLowerCase().hashCode();

		return result;
	}

}
