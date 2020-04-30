package com.sciome.opera.descriptors.padel;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @author Rajarshi Guha
 */
public class ExceptionInfo {
    int serial;
    IAtomContainer molecule;
    String descriptorName;
    Exception exception;

    public ExceptionInfo(int serial, IAtomContainer molecule, Exception exception, String name) {
        this.serial = serial;
        this.molecule = molecule;
        this.exception = exception;
        this.descriptorName = name;
    }

    public String getDescriptorName() {
        return descriptorName;
    }

    public void setDescriptorName(String descriptorName) {
        this.descriptorName = descriptorName;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public IAtomContainer getMolecule() {
        return molecule;
    }

    public void setMolecule(IAtomContainer molecule) {
        this.molecule = molecule;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
