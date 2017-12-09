package retuss;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import mockit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClassDiagramDrawerTest {

    @AfterAll
    public static void resetClassCount() {
        ClassNodeDiagram.resetNodeCount();
    }

    @Nested
    public class クラスアイコンを選択している場合 extends JavaFXComponentTest {
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;

        UtilityJavaFXComponent util;

        @BeforeEach
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            buttons = Arrays.asList( normalButton, classButton, noteButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );
        }

        @Nested
        class クラスに関して {

            @BeforeEach
            public void resetCount() {
                ClassNodeDiagram.resetNodeCount();
            }

            @Test
            public void キャンバスをクリックすると描画する() {
                cdd.setNodeText("ClassName");

                cdd.addDrawnNode(buttons);

                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
            }

            @Test
            public void キャンバスを2回クリックすると2回描画する() {
                cdd.setNodeText("ClassName");

                cdd.addDrawnNode(buttons);
                cdd.addDrawnNode(buttons);

                assertThat(cdd.getNodes().size()).isEqualTo(2);
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(1).getNodeId()).isOne();
            }

            @Test
            public void 削除する() {
                cdd.setNodeText("ClassName");
                cdd.addDrawnNode(buttons);

                cdd.deleteDrawnNode(0);

                assertThat(cdd.getNodes().size()).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
            }

            @Test
            public void 描画済み2つの内1つ目を削除する() {
                // Arrange
                createClasses(cdd, buttons, 0, "FirstClassName", new Point2D(100.0, 200.0));
                createClasses(cdd, buttons, 1, "SecondClassName", new Point2D(500.0, 600.0));

                // Act
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.deleteDrawnNode(cdd.getCurrentNodeNumber());

                // Assert
                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("SecondClassName");
            }

            @Test
            public void 描画済み2つの内2つ目を削除する() {
                // Arrange
                createClasses(cdd, buttons, 0, "FirstClassName", new Point2D(100.0, 200.0));
                createClasses(cdd, buttons, 1, "SecondClassName", new Point2D(500.0, 600.0));

                // Act
                int id = cdd.getNodeDiagramId(500.0, 600.0);
                cdd.deleteDrawnNode(cdd.getCurrentNodeNumber());

                // Assert
                assertThat(id).isOne();
                assertThat(cdd.getCurrentNodeNumber()).isOne();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("FirstClassName");
            }

            @Test
            public void 描画済み2つの内2つ目を削除した後3つ目を描画する() {
                // Arrange
                createClasses(cdd, buttons, 0, "FirstClassName", new Point2D(100.0, 200.0));
                createClasses(cdd, buttons, 1, "SecondClassName", new Point2D(500.0, 600.0));

                // Act
                int id = cdd.getNodeDiagramId(500.0, 600.0);
                cdd.deleteDrawnNode(cdd.getCurrentNodeNumber());

                createClasses(cdd, buttons, 1, "ThirdClassName", new Point2D(300.0, 400.0));

                // Assert
                assertThat(id).isOne();
                assertThat(cdd.getCurrentNodeNumber()).isOne();
                assertThat(cdd.getNodes().size()).isEqualTo(2);
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("FirstClassName");
                assertThat(cdd.getNodes().get(1).getNodeId()).isEqualTo(2);
                assertThat(cdd.getNodes().get(1).getNodeText()).isEqualTo("ThirdClassName");
            }

            @Test
            public void 描画済み3つの内2つ目を削除する() {
                // Arrange
                createClasses(cdd, buttons, 0, "FirstClassName", new Point2D(100.0, 200.0));
                createClasses(cdd, buttons, 1, "SecondClassName", new Point2D(300.0, 400.0));
                createClasses(cdd, buttons, 2, "ThirdClassName", new Point2D(500.0, 600.0));

                // Act
                int id = cdd.getNodeDiagramId(300.0, 400.0);
                cdd.deleteDrawnNode(cdd.getCurrentNodeNumber());

                // Assert
                assertThat(id).isOne();
                assertThat(cdd.getNodes().size()).isEqualTo(2);
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("FirstClassName");
                assertThat(cdd.getNodes().get(1).getNodeId()).isEqualTo(2);
                assertThat(cdd.getNodes().get(1).getNodeText()).isEqualTo("ThirdClassName");
            }

            @Test
            public void 描画済み3つの内2つ目を削除した後4つ目を描画する() {
                createClasses(cdd, buttons, 0, "FirstClassName", new Point2D(100.0, 200.0));
                createClasses(cdd, buttons, 1, "SecondClassName", new Point2D(300.0, 400.0));
                createClasses(cdd, buttons, 2, "ThirdClassName", new Point2D(500.0, 600.0));
                int id = cdd.getNodeDiagramId(300.0, 400.0);
                cdd.deleteDrawnNode(cdd.getCurrentNodeNumber());

                createClasses(cdd, buttons, 2, "FourthClassName", new Point2D(700.0, 800.0));

                assertThat(id).isOne();
                assertThat(cdd.getNodes().size()).isEqualTo(3);
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(2).getNodeId()).isEqualTo(3);
                assertThat(cdd.getNodes().get(2).getNodeText()).isEqualTo("FourthClassName");
            }

            @Test
            public void 追加する際に空文字を入力した場合は追加しない(@Mocked ClassNodeDiagram mock) {
                cdd.setNodeText("");
                cdd.setMouseCoordinates(100.0, 200.0);
                cdd.addDrawnNode(buttons);

                assertThat(cdd.getNodes().size()).isZero();
                cdd.allReDrawNode();

                new Verifications() {{
                    mock.draw();
                    times = 0;
                }};
            }

            @Test
            public void クラス名を変更する() {
                cdd.setNodeText("ClassName");
                cdd.addDrawnNode(buttons);

                cdd.changeDrawnNodeText(0, ContentType.Title, 0, "ChangedClassName");

                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ChangedClassName");
            }

            @Test
            public void クラス名を変更する際に空文字を入力した場合は変更しない() {
                createClasses(cdd, buttons, 0, "NotChangedClassName", new Point2D(100.0, 200.0));

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.changeDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Title, 0, "");

                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("NotChangedClassName");
            }

            @Test
            public void 描画済み2つの内1つ目のクラス名を変更する() {
                // Arrange
                createClasses(cdd, buttons, 0, "ClassName", new Point2D(100.0, 200.0));
                createClasses(cdd, buttons, 1, "NotChangingClassName", new Point2D(500.0, 600.0));

                // Act
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.changeDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Title, 0, "ChangedClassName");

                // Assert
                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isEqualTo(2);
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ChangedClassName");
                assertThat(cdd.getNodes().get(1).getNodeId()).isOne();
                assertThat(cdd.getNodes().get(1).getNodeText()).isEqualTo("NotChangingClassName");
            }

            @Test
            public void 幅と高さを返す() {
                double expectedWidth = 100.0;
                double expectedHeight = 80.0;
                createClasses(cdd, buttons, 0, "ClassName", new Point2D(100.0, 200.0));

                double actualWidth = cdd.getNodeWidth(0);
                double actualHeight = cdd.getNodeHeight(0);

                assertThat(actualWidth).isEqualTo(expectedWidth);
                assertThat(actualHeight).isEqualTo(expectedHeight);
            }
        }

        @Nested
        class クラスの属性に関して {

            @BeforeEach
            public void resetCount() {
                ClassNodeDiagram.resetNodeCount();
            }

            @BeforeEach
            public void setUp() {
                createClasses(cdd, buttons, 0, "ClassName", new Point2D(100.0, 200.0));
            }

            @Test
            public void 追加する() {
                // Arrange
                int id = -1;
                List<String> attributions = Arrays.asList(
                        "- content1 : int",
                        "- content2 : double",
                        "- content3 : char");

                // Act
                for (String attribution : attributions) {
                    id = cdd.getNodeDiagramId(100.0, 200.0);
                    cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, attribution);
                }

                // Assert
                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeTextList(0, ContentType.Attribution).size()).isEqualTo(attributions.size());
                for (int i = 0; i < attributions.size(); i++) {
                    assertThat(cdd.getDrawnNodeTextList(0, ContentType.Attribution).get(i)).isEqualTo(attributions.get(i));
                }
            }

            @Test
            public void 追加する際に空文字を入力した場合は追加しない() {
                String attribution = "";

                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, attribution);

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeTextList(0, ContentType.Attribution).size()).isZero();
            }

            @Test
            public void リストを取得する() {

                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int");

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getNodes().get(0).getNodeContentText(ContentType.Attribution, 0)).isEqualTo("- content : int");
            }

            @Test
            public void 変更する() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.changeDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, 0, "- content : double");

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getNodes().get(0).getNodeContentText(ContentType.Attribution, 0)).isEqualTo("- content : double");
            }

            @Test
            public void 変更する際に空文字を入力した場合は変更しない() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.changeDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, 0, "");

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getNodes().get(0).getNodeContentText(ContentType.Attribution, 0)).isEqualTo("- content : int");
            }

            @Test
            public void 削除する() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.deleteDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, 0);

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeTextList(0, ContentType.Attribution).size()).isZero();
            }

            @Test
            public void 非表示にする() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content1 : int");
                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content2 : double");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.setDrawnNodeContentBoolean(cdd.getCurrentNodeNumber(), ContentType.Attribution, ContentType.Indication, 0, false);

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeContentsBooleanList(0, ContentType.Attribution, ContentType.Indication).size()).isEqualTo(2);
                assertThat(cdd.getDrawnNodeContentsBooleanList(0, ContentType.Attribution, ContentType.Indication).get(0)).isFalse();
                assertThat(cdd.getDrawnNodeContentsBooleanList(0, ContentType.Attribution, ContentType.Indication).get(1)).isTrue();
            }
        }

        @Nested
        class クラスの操作に関して {

            @BeforeEach
            public void resetCount() {
                ClassNodeDiagram.resetNodeCount();
            }

            @BeforeEach
            public void setUp() {
                createClasses(cdd, buttons, 0, "ClassName", new Point2D(100.0, 200.0));
            }

            @Test
            public void 追加する() {
                // Arrange
                int id = -1;
                List<String> operations = Arrays.asList("- content1 : int", "- content2 : double", "- content3 : char");

                // Act
                for (String operation : operations) {
                    id = cdd.getNodeDiagramId(100.0, 200.0);
                    cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, operation);
                }

                // Assert
                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeTextList(0, ContentType.Operation).size()).isEqualTo(operations.size());
                for (int i = 0; i < operations.size(); i++) {
                    assertThat(cdd.getDrawnNodeTextList(0, ContentType.Operation).get(i)).isEqualTo(operations.get(i));
                }
            }

            @Test
            public void 追加する際に空文字を入力した場合は追加しない() {
                String attribution = "";

                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, attribution);

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeTextList(0, ContentType.Operation).size()).isZero();
            }

            @Test
            public void 変更する() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, "+ content() : int");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.changeDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, 0, "+ content() : double");

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getNodes().get(0).getNodeContentText(ContentType.Operation, 0)).isEqualTo("+ content() : double");
            }

            @Test
            public void 変更する際に空文字を入力した場合は変更しない() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, "+ content() : int");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.changeDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, 0, "");

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getNodes().get(0).getNodeContentText(ContentType.Operation, 0)).isEqualTo("+ content() : int");
            }

            @Test
            public void 削除する() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, "- content() : int");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.deleteDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, 0);

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeTextList(0, ContentType.Operation).size()).isZero();
            }

            @Test
            public void 非表示にする() {

                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, "- content1() : int");
                cdd.getNodeDiagramId(100.0, 200.0);
                cdd.addDrawnNodeText(cdd.getCurrentNodeNumber(), ContentType.Operation, "- content2() : double");
                int id = cdd.getNodeDiagramId(100.0, 200.0);
                cdd.setDrawnNodeContentBoolean(cdd.getCurrentNodeNumber(), ContentType.Operation, ContentType.Indication, 0, false);

                assertThat(id).isZero();
                assertThat(cdd.getCurrentNodeNumber()).isZero();
                assertThat(cdd.getNodes().size()).isOne();
                assertThat(cdd.getNodes().get(0).getNodeId()).isZero();
                assertThat(cdd.getNodes().get(0).getNodeText()).isEqualTo("ClassName");
                assertThat(cdd.getDrawnNodeContentsBooleanList(0, ContentType.Operation, ContentType.Indication).size()).isEqualTo(2);
                assertThat(cdd.getDrawnNodeContentsBooleanList(0, ContentType.Operation, ContentType.Indication).get(0)).isFalse();
                assertThat(cdd.getDrawnNodeContentsBooleanList(0, ContentType.Operation, ContentType.Indication).get(1)).isTrue();
            }
        }
    }

    @Nested
    public class ノートアイコンを選択している場合 extends JavaFXComponentTest {
        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;

        UtilityJavaFXComponent util;

        @BeforeEach
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            buttons = Arrays.asList( normalButton, classButton, noteButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, noteButton );
        }

        @Test
        public void キャンバスをクリックするとノートを描画する( @Mocked NoteNodeDiagram mock ) {
            // Arrange
            cdd.setNodeText( "Note" );
            cdd.addDrawnNode( buttons );

            // Act
            cdd.allReDrawNode();

            // Assert
            assertThat( cdd.getNodes().size() ).isOne();

            new Verifications() {{
                mock.draw();
                times = 1;
            }};
        }
    }

    @Nested
    public class ノーマルアイコンを選択している場合 extends JavaFXComponentTest {
        @Tested
        ClassDiagramDrawer cdd;

        List<Button> buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;

        UtilityJavaFXComponent util;

        @BeforeEach
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            buttons = Arrays.asList( normalButton, classButton, noteButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );
        }

        @Test
        public void キャンバスをクリックしても何も描画しない( @Mocked ClassNodeDiagram classNodeDiagram, @Mocked NoteNodeDiagram noteNodeDiagram ) {
            // Arrange
            cdd.setNodeText( "Item" );

            // Act
            cdd.addDrawnNode( buttons );

            // Arrange
            assertThat( cdd.getNodes().size() ).isZero();

            new Verifications() {{
                classNodeDiagram.draw();
                times = 0;
                noteNodeDiagram.draw();
                times = 0;
            }};
        }
    }

    @Nested
    public class コンポジションアイコンを選択している場合 extends JavaFXComponentTest {
        @Tested
        ClassDiagramDrawer cdd;

        List<Button> buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;
        Button compositionButton;

        UtilityJavaFXComponent util;

        @BeforeEach
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button("Normal");
            classButton = new Button("Class");
            noteButton = new Button("Note");
            compositionButton = new Button("Composition");
            buttons = Arrays.asList(normalButton, classButton, noteButton, compositionButton);

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout(buttons, compositionButton);
        }

        @Test
        public void キャンバスをクリックしても何も描画しない( @Mocked ClassNodeDiagram classNodeDiagram, @Mocked NoteNodeDiagram noteNodeDiagram ) {
            cdd.setNodeText( "Item" );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size() ).isZero();

            new Verifications() {{
                classNodeDiagram.draw();
                times = 0;
                noteNodeDiagram.draw();
                times = 0;
            }};
        }
    }

    @Nested
    public class クラスを3つ記述後にコンポジションアイコンを選択している場合 extends JavaFXComponentTest {
        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;
        Button compositionButton;
        Point2D firstClass;
        Point2D secondClass;
        Point2D thirdClass;
        Point2D betweenFirstAndSecondClass;
        Point2D betweenFirstAndThirdClass;

        UtilityJavaFXComponent util;

        @BeforeEach
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button("Normal");
            classButton = new Button("Class");
            noteButton = new Button("Note");
            compositionButton = new Button("Composition");
            buttons = Arrays.asList(normalButton, classButton, noteButton, compositionButton);
            firstClass = new Point2D(100.0, 200.0);
            secondClass = new Point2D(500.0, 600.0);
            thirdClass = new Point2D(100.0, 600.0);
            betweenFirstAndSecondClass = new Point2D(300.0, 400.0);
            betweenFirstAndThirdClass = new Point2D(100.0, 400.0);

            util = new UtilityJavaFXComponent();
            buttons = util.setAllDefaultButtonIsFalseWithout(buttons, classButton);

            ClassNodeDiagram.resetNodeCount();

            cdd.setNodeText("FirstClassName");
            createClasses( cdd, buttons, 0, "FirstClassName", firstClass );
            createClasses( cdd, buttons, 1, "SecondClassName", secondClass );
            createClasses( cdd, buttons, 2, "ThirdClassName", thirdClass );
            buttons = util.setAllDefaultButtonIsFalseWithout(buttons, compositionButton);
        }

        @Test
        public void 描画済みクラスの1つ目をクリックするとコンポジションの描画を待機し正規ノードを待機していなかったとする() {

            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );

            assertThat( actual ).isFalse();
            assertThat( cdd.getNowStateType() ).isEqualByComparingTo( ContentType.Composition );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void 描画済みクラスの1つ目をクリックした後に2つ目をクリックするとコンポジションの描画待機を解除し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void 描画済みクラスの1つ目をクリックした後にクラスを描画していない箇所をクリックするとコンポジションの描画待機を解除し正規ノードを待機していなかったとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX() + 100.0, secondClass.getY() + 100.0 );

            assertThat( actual ).isFalse();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void 描画済みクラスの1つ目をクリックした後に1つ目をクリックするとコンポジションの描画待機を解除し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void 描画済みクラスの1つ目をコンポジションとしてクリックした後に2つ目を汎化としてクリックすると汎化の描画を待機し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Generalization );
            assertThat( cdd.getCurrentNodeNumber() ).isOne();
        }

        @Test
        public void 描画済みクラスの1つ目をコンポジションとしてクリックして2つ目を汎化としてクリックした後に3回目と4回目をコンポジションとしてクリックするとコンポジションの描画待機を解除し正規ノードを待機していたとする() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void 描画済みクラスの1つ目をコンポジションとしてクリックした後に2回目と3回目を汎化としてクリックすると汎化の描画を待機し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Generalization );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();

            actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );
            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void 描画済みクラスの2つクリックするとコンポジション関係を描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getCurrentNodeNumber() ).isOne();
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ) ).isEqualTo( "- composition" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ) ).isOne();
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ) ).isZero();
        }

        @Test
        public void 描画済みクラスの2つのクラス間をクリックすると真を返すがそれ以外の箇所では偽を返す() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );

            boolean actualTrue = cdd.isAlreadyDrawnAnyDiagram( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            boolean actualFalse = cdd.isAlreadyDrawnAnyDiagram( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() + 100.0 );

            assertThat( actualTrue ).isTrue();
            assertThat( actualFalse ).isFalse();
        }

        @Test
        public void キャンバスに描画している一番上の関係の内容を返す() {
            RelationshipAttribution expected = new RelationshipAttribution( "- composition" );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );
            cdd.searchDrawnAnyDiagramType( secondClass.getX(), secondClass.getY() );

            RelationshipAttribution actual = cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            assertThat( actual.getName() ).isEqualTo( expected.getName() );
        }

        @Test
        public void キャンバスに描画している一番上の関係の内容を変更する() {
            RelationshipAttribution expected = new RelationshipAttribution( "- changedComposition" );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );
            cdd.searchDrawnAnyDiagramType( secondClass.getX(), secondClass.getY() );

            cdd.changeDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY(), expected.getName() );
            RelationshipAttribution actual = cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            assertThat( actual.getName() ).isEqualTo( expected.getName() );
        }

        @Test
        public void キャンバスに描画している一番上の関係の内容を削除する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );
            cdd.searchDrawnAnyDiagramType( secondClass.getX(), secondClass.getY() );

            cdd.deleteDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            RelationshipAttribution actual = cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            assertThat( actual ).isNull();
        }

        @Test
        public void キャンバスに描画しているクラス2つのコンポジション関係を複数描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition1", secondClass.getX(), secondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition2", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ) ).isEqualTo( "- composition1" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ) ).isOne();
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ) ).isZero();
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 1 ) ).isEqualTo( "- composition2" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 1 ) ).isOne();
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 1 ) ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラス2つのコンポジション関係を描画する途中で一度描画していない箇所を選択してから描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            cdd.setMouseCoordinates( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ) ).isEqualTo( "- composition" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ) ).isOne();
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ) ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラス3つのコンポジション関係を2つ描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition1", secondClass.getX(), secondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, thirdClass.getX(), thirdClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition2", thirdClass.getX(), thirdClass.getY() );

            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ) ).isEqualTo( "- composition1" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ) ).isOne();
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ) ).isZero();
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 1 ) ).isEqualTo( "- composition2" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 1 ) ).isEqualTo( 2 );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 1 ) ).isZero();
            assertThat( cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() ).getName() ).isEqualTo( "- composition1" );
            assertThat( cdd.searchDrawnEdge( betweenFirstAndThirdClass.getX(), betweenFirstAndThirdClass.getY() ).getName() ).isEqualTo( "- composition2" );
        }
    }

    @Nested
    public class クラスを3つ記述後に汎化アイコンを選択している場合 extends JavaFXComponentTest {
        @Tested
        ClassDiagramDrawer cdd;

        List<Button> buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;
        Button generalizationButton;
        Point2D firstClass;
        Point2D secondClass;
        Point2D thirdClass;
        Point2D betweenFirstAndSecondClass;
        Point2D betweenFirstAndThirdClass;

        UtilityJavaFXComponent util;

        @BeforeEach
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button("Normal");
            classButton = new Button("Class");
            noteButton = new Button("Note");
            generalizationButton = new Button("Generalization");
            buttons = Arrays.asList(normalButton, classButton, noteButton, generalizationButton);
            firstClass = new Point2D(100.0, 200.0);
            secondClass = new Point2D(500.0, 600.0);
            thirdClass = new Point2D(100.0, 600.0);
            betweenFirstAndSecondClass = new Point2D(300.0, 400.0);
            betweenFirstAndThirdClass = new Point2D(100.0, 400.0);

            util = new UtilityJavaFXComponent();
            buttons = util.setAllDefaultButtonIsFalseWithout(buttons, classButton);

            ClassNodeDiagram.resetNodeCount();

            createClasses( cdd, buttons, 0, "FirstClassName", firstClass );
            createClasses( cdd, buttons, 1, "SecondClassName", secondClass );
            createClasses( cdd, buttons, 2, "ThirdClassName", thirdClass );
            buttons = util.setAllDefaultButtonIsFalseWithout(buttons, generalizationButton);
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックすると汎化の描画を待機し正規ノードを待機していなかったとする() {

            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );

            assertThat( actual ).isFalse();
            assertThat( cdd.getNowStateType() ).isEqualByComparingTo( ContentType.Generalization );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックした後に2つ目をクリックすると汎化の描画待機を解除し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックした後にクラスを描画していない箇所をクリックすると汎化の描画待機を解除し正規ノードを待機していなかったとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX() + 100.0, secondClass.getY() + 100.0 );

            assertThat( actual ).isFalse();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックした後に1つ目をクリックすると汎化の描画待機を解除し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目を汎化としてクリックした後に2つ目を汎化としてクリックするとコンポジションの描画を待機し正規ノードを待機していたとする() {

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Composition );
            assertThat( cdd.getCurrentNodeNumber() ).isOne();
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目を汎化としてクリックして2つ目をコンポジションとしてクリックした後に3回目と4回目を汎化としてクリックすると汎化の描画待機を解除し正規ノードを待機していたとする() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );

            assertThat( actual ).isTrue();
            assertThat( cdd.getNowStateType() ).isEqualTo( ContentType.Undefined );
            assertThat( cdd.getCurrentNodeNumber() ).isZero();
        }

        @Test
        public void キャンバスに描画しているクラスを2つクリックすると汎化関係を描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getCurrentNodeNumber() ).isOne();
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ) ).isEqualTo( "" );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ) ).isOne();
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ) ).isZero();
        }

        @Test
        public void キャンバスに関係を描画している2つのクラス間をクリックすると真を返すがそれ以外の箇所では偽を返す() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "", secondClass.getX(), secondClass.getY() );

            boolean actualTrue = cdd.isAlreadyDrawnAnyDiagram( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            boolean actualFalse = cdd.isAlreadyDrawnAnyDiagram( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() + 100.0 );

            assertThat( actualTrue ).isTrue();
            assertThat( actualFalse ).isFalse();
        }
    }

    private void createClasses( ClassDiagramDrawer cdd, List<Button> buttons, int classCount, String className, Point2D classPosition ) {
        cdd.setNodeText( className );
        cdd.setMouseCoordinates( classPosition.getX(), classPosition.getY() );
        cdd.addDrawnNode( buttons );
        ( ( ClassNodeDiagram ) cdd.getNodes().get( classCount ) ).calculateWidthAndHeight( 100.0, 80.0 );
    }
}