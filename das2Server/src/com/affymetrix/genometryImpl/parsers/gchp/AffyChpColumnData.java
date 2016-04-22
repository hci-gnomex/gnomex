// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.gchp;

import java.io.PrintStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;
import cern.colt.list.ShortArrayList;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.list.DoubleArrayList;
import cern.colt.list.ByteArrayList;

public final class AffyChpColumnData
{
    String name;
    AffyDataType type;
    int size;
    Object theData;
    ByteArrayList dataByte;
    DoubleArrayList dataDouble;
    IntArrayList dataInt;
    FloatArrayList dataFloat;
    ShortArrayList dataShort;
    ArrayList<CharSequence> dataString;
    AffySingleChromData singleChromData;
    
    AffyChpColumnData(final AffySingleChromData singleChromData, final String name, final AffyDataType type, final int size) {
        this.theData = null;
        this.dataByte = null;
        this.dataDouble = null;
        this.dataInt = null;
        this.dataFloat = null;
        this.dataShort = null;
        this.dataString = null;
        this.singleChromData = singleChromData;
        this.name = name;
        this.type = type;
        this.size = size;
        switch (this.type) {
            case INT8: {
                final ByteArrayList list = new ByteArrayList();
                this.dataByte = list;
                this.theData = list;
                break;
            }
            case UINT8: {
                final ShortArrayList list2 = new ShortArrayList();
                this.dataShort = list2;
                this.theData = list2;
                break;
            }
            case INT16: {
                final ShortArrayList list3 = new ShortArrayList();
                this.dataShort = list3;
                this.theData = list3;
                break;
            }
            case UINT16: {
                final IntArrayList list4 = new IntArrayList();
                this.dataInt = list4;
                this.theData = list4;
                break;
            }
            case INT32: {
                final IntArrayList list5 = new IntArrayList();
                this.dataInt = list5;
                this.theData = list5;
                break;
            }
            case UINT32: {
                final IntArrayList list6 = new IntArrayList();
                this.dataInt = list6;
                this.theData = list6;
                break;
            }
            case FLOAT: {
                final FloatArrayList list7 = new FloatArrayList();
                this.dataFloat = list7;
                this.theData = list7;
                break;
            }
            case DOUBLE: {
                final DoubleArrayList list8 = new DoubleArrayList();
                this.dataDouble = list8;
                this.theData = list8;
                break;
            }
            case TEXT_ASCII: {
                final ArrayList<CharSequence> list9 = new ArrayList<CharSequence>();
                this.dataString = list9;
                this.theData = list9;
                break;
            }
            case TEXT_UTF16BE: {
                final ArrayList<CharSequence> list10 = new ArrayList<CharSequence>();
                this.dataString = list10;
                this.theData = list10;
                break;
            }
            default: {
                throw new RuntimeException("Can't parse that type: " + type);
            }
        }
    }
    
    void addData(final DataInputStream dis) throws IOException {
        switch (this.type) {
            case INT8: {
                this.dataByte.add(dis.readByte());
                break;
            }
            case UINT8: {
                this.dataShort.add((short)dis.readUnsignedByte());
                break;
            }
            case INT16: {
                this.dataShort.add(dis.readShort());
                break;
            }
            case UINT16: {
                this.dataInt.add(dis.readUnsignedShort());
                break;
            }
            case INT32: {
                this.dataInt.add(dis.readInt());
                break;
            }
            case UINT32: {
                this.dataInt.add(dis.readInt());
                break;
            }
            case FLOAT: {
                this.dataFloat.add(dis.readFloat());
                break;
            }
            case DOUBLE: {
                this.dataDouble.add(dis.readDouble());
                break;
            }
            case TEXT_ASCII: {
                this.dataString.add(AffyGenericChpFile.parseString(dis));
                break;
            }
            case TEXT_UTF16BE: {
                this.dataString.add(AffyGenericChpFile.parseWString(dis));
                break;
            }
            default: {
                throw new RuntimeException("Can't parse that type: " + this.type);
            }
        }
    }
    
    int getByteLength() {
        return this.size;
    }
    
    Object getData() {
        return this.theData;
    }
    
    @Override
    public String toString() {
        String s = this.getClass().getName() + ": " + this.name + ", " + this.type + ", " + this.size + ", " + this.theData.getClass().getName() + ":  ";
        for (int i = 0; i < 5; ++i) {
            switch (this.type) {
                case INT8: {
                    s = s + this.dataByte.get(i) + ", ";
                    break;
                }
                case UINT8: {
                    s = s + this.dataInt.get(i) + ", ";
                    break;
                }
                case INT16: {
                    s = s + this.dataShort.get(i) + ", ";
                    break;
                }
                case UINT16: {
                    s = s + this.dataInt.get(i) + ", ";
                    break;
                }
                case INT32: {
                    s = s + this.dataInt.get(i) + ", ";
                    break;
                }
                case UINT32: {
                    s = s + this.dataInt.get(i) + ", ";
                    break;
                }
                case FLOAT: {
                    s = s + this.dataFloat.get(i) + ", ";
                    break;
                }
                case DOUBLE: {
                    s = s + this.dataDouble.get(i) + ", ";
                    break;
                }
                case TEXT_ASCII: {
                    s = s + (Object)this.dataString.get(i) + ", ";
                    break;
                }
                case TEXT_UTF16BE: {
                    s = s + (Object)this.dataString.get(i) + ", ";
                    break;
                }
                default: {
                    throw new RuntimeException("Can't parse that type: " + this.type);
                }
            }
        }
        s += " ...";
        return s;
    }
    
    void dump(final PrintStream str) {
        str.println(this.toString());
    }
}
