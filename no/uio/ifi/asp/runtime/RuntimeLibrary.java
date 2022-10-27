// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // len
        // new RuntimeFunc("len") er en anonym subklasse av RuntimeFunc
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0) System.out.print(" ");
                    System.out.print(actualParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

         // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                double floatVal = actualParams.get(0).getFloatValue("float", where);
                return new RuntimeFloatValue(floatVal);
            }
        });

        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.println(actualParams.get(0).toString());
                String input = keyboard.nextLine();
                return new RuntimeStringValue(input);
            }
        });

        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                long intVal = actualParams.get(0).getIntValue("int", where);
                return new RuntimeIntValue(intVal);
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                long start = actualParams.get(0).getIntValue("range start", where);
                long end = actualParams.get(1).getIntValue("range end", where);

                // f. eks [2,3,4,5]
                ArrayList<RuntimeValue> rangeList = new ArrayList<>();
                for (long i = start; i < end; i++) {
                    rangeList.add(new RuntimeIntValue(i));
                }

                return new RuntimeListValue(rangeList);
            }
        });

        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                String strVal = actualParams.get(0).getStringValue("sstr", where);
                return new RuntimeStringValue(strVal);
            }
        });
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
            int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}
