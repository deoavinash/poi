/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.poi.hwpf.extractor;

import java.io.FileInputStream;

import junit.framework.TestCase;

/**
 * Tests for bugs with the WordExtractor
 *
 * @author Nick Burch (nick at torchbox dot com)
 */
public class TestWordExtractorBugs extends TestCase {
	private String dirname;
    protected void setUp() throws Exception {
		dirname = System.getProperty("HWPF.testdata.path");
    }
    
    public void testProblemMetadata() throws Exception {
		String filename = dirname + "/ProblemExtracting.doc";
		WordExtractor extractor = 
			new WordExtractor(new FileInputStream(filename));
		
		// Check it gives text without error
		extractor.getText();
		extractor.getParagraphText();
		extractor.getTextFromPieces();
    }			
    
}