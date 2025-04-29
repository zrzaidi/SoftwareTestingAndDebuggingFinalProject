package test.whitebox;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoadFileTest {

    private map.LoadFile loadFile;
    private static final String TEST_FOLDER = "mapSaves";
    private File testDirectory;
    
    @Before
    public void setUp() throws Exception {
        try {
            testDirectory = new File(TEST_FOLDER);
            if (!testDirectory.exists()) {
                testDirectory.mkdir();
            }
            
            // Initialize LoadFile object
            loadFile = new map.LoadFile();
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }
    
    @After
    public void tearDown() throws Exception {
        try {
            // Clean up test files
            File[] files = testDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith("test_")) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            fail("Tear down failed: " + e.getMessage());
        }
    }
    
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        try {
            Field field = map.LoadFile.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            fail("Failed to set private field: " + e.getMessage());
        }
    }
    
    private Object getPrivateField(Object target, String fieldName) throws Exception {
        try {
            Field field = map.LoadFile.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            fail("Failed to get private field: " + e.getMessage());
            return null;
        }
    }
    
    private File createTestMapFile(String name, int width, int height, String userInput, String mapData) throws IOException {
        try {
            File file = new File(TEST_FOLDER + "/" + name + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(String.valueOf(width));
            writer.newLine();
            writer.write(String.valueOf(height));
            writer.newLine();
            writer.write(userInput);
            writer.newLine();
            writer.write(mapData);
            writer.close();
            return file;
        } catch (IOException e) {
            fail("Failed to create test map file: " + e.getMessage());
            throw e; // Re-throw after logging
        }
    }
    
    @Test
    public void testConstructor() {
        try {
            assertNotNull(loadFile.getListofFiles());
        } catch (Exception e) {
            fail("testConstructor failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testListFilesForFolder() throws Exception {
        try {
            createTestMapFile("test_map1", 10, 10, "user input 1", "map data 1");
            createTestMapFile("test_map2", 20, 20, "user input 2", "map data 2");
            
            setPrivateField(loadFile, "files", new ArrayList<String>());
            loadFile.listFilesforFolder();
            
            ArrayList<String> files = loadFile.getListofFiles();
            assertTrue("File list should contain test_map1", files.contains("test_map1"));
            assertTrue("File list should contain test_map2", files.contains("test_map2"));
        } catch (Exception e) {
            fail("testListFilesForFolder failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testListFilesForFolder_WithNonTextFiles() throws Exception {
        try {
            File nonTextFile = new File(TEST_FOLDER + "/test_other.dat");
            nonTextFile.createNewFile();
            
            createTestMapFile("test_map3", 10, 10, "user input", "map data");
            
            setPrivateField(loadFile, "files", new ArrayList<String>());
            loadFile.listFilesforFolder();
            
            ArrayList<String> files = loadFile.getListofFiles();
            assertTrue("File list should contain test_map3", files.contains("test_map3"));
            assertTrue("File list should contain test_other", files.contains("test_other"));
            
            nonTextFile.delete();
        } catch (Exception e) {
            fail("testListFilesForFolder_WithNonTextFiles failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadFile() throws Exception {
        try {
            createTestMapFile("test_map_load", 15, 25, "sample input", "sample map data");
            setPrivateField(loadFile, "mapList", new ArrayList<map.Map>());
            
            map.Map result = loadFile.loadFile("test_map_load");
            
            assertNotNull("Loaded map should not be null", result);
            assertEquals("Width should be 15", 15, getPrivateField(loadFile, "width"));
            assertEquals("Height should be 25", 25, getPrivateField(loadFile, "height"));
            assertEquals("User input should match", "sample input", getPrivateField(loadFile, "userInput"));
            assertTrue("Map info should contain sample map data", 
                    ((String)getPrivateField(loadFile, "mapInfo")).contains("sample map data"));
        } catch (Exception e) {
            fail("testLoadFile failed: " + e.getMessage());
        }
    }
    
    @Test(expected = IOException.class)
    public void testLoadFile_FileNotFound() throws Exception {
        try {
            loadFile.loadFile("non_existent_file");
        } catch (IOException e) {
            throw e; 
        }
    }
    
    @Test
    public void testLoadFile_EmptyFile() throws Exception {
        try {
            File emptyFile = new File(TEST_FOLDER + "/test_empty.txt");
            emptyFile.createNewFile();
            
            setPrivateField(loadFile, "mapList", new ArrayList<map.Map>());
            
            loadFile.loadFile("test_empty");
            fail("Should have thrown an exception for empty file");
        } catch (Exception e) {
            // Expected exception
        } finally {
            // Clean up
            new File(TEST_FOLDER + "/test_empty.txt").delete();
        }
    }
    
    @Test
    public void testGetAllMap() throws Exception {
        try {
            createTestMapFile("test_all_map1", 10, 10, "input 1", "data 1");
            createTestMapFile("test_all_map2", 20, 20, "input 2", "data 2");
            
            ArrayList<String> filesList = new ArrayList<String>();
            filesList.add("test_all_map1");
            filesList.add("test_all_map2");
            setPrivateField(loadFile, "files", filesList);
            
            setPrivateField(loadFile, "mapList", new ArrayList<map.Map>());
            ArrayList<map.Map> result = loadFile.getAllMap();
            
            assertEquals("Should have loaded 2 maps", 2, result.size());
            assertNotNull("First map should not be null", result.get(0));
            assertNotNull("Second map should not be null", result.get(1));
        } catch (Exception e) {
            fail("testGetAllMap failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetAllMap_WithInvalidFile() throws Exception {
        try {
            createTestMapFile("test_valid", 10, 10, "input", "data");
            File invalidFile = new File(TEST_FOLDER + "/test_invalid.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(invalidFile));
            writer.write("not enough data");
            writer.close();
            
            ArrayList<String> filesList = new ArrayList<String>();
            filesList.add("test_valid");
            filesList.add("test_invalid");
            setPrivateField(loadFile, "files", filesList);
            
            setPrivateField(loadFile, "mapList", new ArrayList<map.Map>());
            ArrayList<map.Map> result = loadFile.getAllMap();
            
            assertEquals("Should have loaded 1 valid map", 1, result.size());
            assertNotNull("First map should not be null", result.get(0));

            invalidFile.delete();
        } catch (Exception e) {
            fail("testGetAllMap_WithInvalidFile failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateMap() throws Exception {
        try {
            setPrivateField(loadFile, "width", 30);
            setPrivateField(loadFile, "height", 40);
            setPrivateField(loadFile, "userInput", "test input");
            
            map.Map result = loadFile.createMap();
            assertNotNull("Created map should not be null", result);
        } catch (Exception e) {
            fail("testCreateMap failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetListofFiles() throws Exception {
        try {
            createTestMapFile("test_file_list1", 10, 10, "input", "data");
            createTestMapFile("test_file_list2", 20, 20, "input", "data");
            
            loadFile.listFilesforFolder();
            ArrayList<String> result = loadFile.getListofFiles();
            
            assertNotNull("File list should not be null", result);
            assertTrue("File list should contain test_file_list1", result.contains("test_file_list1"));
            assertTrue("File list should contain test_file_list2", result.contains("test_file_list2"));
        } catch (Exception e) {
            fail("testGetListofFiles failed: " + e.getMessage());
        }
    }
}