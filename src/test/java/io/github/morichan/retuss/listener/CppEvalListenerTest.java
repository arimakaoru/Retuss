package io.github.morichan.retuss.listener;

//import io.github.morichan.retuss.language.cpp.MemberVariable;
import io.github.morichan.retuss.language.cpp.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

import io.github.morichan.retuss.language.cpp.Cpp;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import io.github.morichan.retuss.parser.cpp.CPP14Lexer;
import io.github.morichan.retuss.parser.cpp.CPP14Parser;
import java.util.Arrays;
import java.util.List;
class CppEvalListenerTest {

    private CppEvalListener cppEvalListener = new CppEvalListener();
    CppEvalListener obj;

    CPP14Lexer lexer;
    CommonTokenStream tokens;
    CPP14Parser parser;
    ParseTree tree;
    ParseTreeWalker walker;

//    private Cpp cpp;
//    private String className;
//    private String extendedClassName;

//    @Test
//    void enterClassspecifier() {
//    }



    @Test
    void 構文解析時にエラーが出ないか確認する() {
        try {
            walker.walk(cppEvalListener, tree);
        } catch (NullPointerException e) {
            fail("ParseTreeObjectNullError");
        }
    }



    @Test
    void クラス名を返す() {
        init("class cppClassName {};");
        String expected = "cppClassName";

        String actual = obj.getCpp().getClasses().get(0).getName();

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void 一番最後のメンバ変数を返す() {
        init("class cppClassName {private: int x;};");
        String expected = "x";

        String actual = obj.getCpp().getClasses().get(0).getMemberVariables().get(0).getName();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void フィールドを2つ返す() {
        init("class CppClass {private: int number; protected: double point;};");
        List<MemberVariable> expected = Arrays.asList(
                new MemberVariable(new Type("int"), "number"),
                new MemberVariable(new Type("double"), "point"));
        expected.get(0).setAccessSpecifier(AccessSpecifier.Private);
        expected.get(1).setAccessSpecifier(AccessSpecifier.Protected);

        List<MemberVariable> actual = obj.getCpp().getClasses().get(0).getMemberVariables();

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i)).isEqualToComparingFieldByFieldRecursively(expected.get(i));
        }
    }



    @Test
    void メソッドを1つ返す() {
        init("class cppClassXYZ {public: void print() {}};");
        MemberFunction expected = new MemberFunction(new Type("void"), "print");

        MemberFunction actual = obj.getCpp().getClasses().get(0).getMemberFunctions().get(0);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void メソッドを3つ返す() {
        init("class CppClass {public: void print() {} protected: int getItem() {} private: void setItem() {}};");
        List<MemberFunction> expected = Arrays.asList(
                new MemberFunction(new Type("void"), "print"),
                new MemberFunction(new Type("int"), "getItem"),
                new MemberFunction(new Type("void"), "setItem")
        );
        expected.get(0).setAccessSpecifier(AccessSpecifier.Public);
        expected.get(1).setAccessSpecifier(AccessSpecifier.Protected);
        expected.get(2).setAccessSpecifier(AccessSpecifier.Private);
     //   expected.get(2).addArgument(new Argument(new Type("int"), "item"));

        List<MemberFunction> actual = obj.getCpp().getClasses().get(0).getMemberFunctions();

        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i)).isEqualToComparingFieldByFieldRecursively(expected.get(i));
        }
    }

    private void init(String cppCode) {
        lexer = new CPP14Lexer(CharStreams.fromString(cppCode));
        tokens = new CommonTokenStream(lexer);
        parser = new CPP14Parser(tokens);
        tree = parser.translationunit();
        walker = new ParseTreeWalker();
        obj = new CppEvalListener();
        walker.walk(obj, tree);
    }
}