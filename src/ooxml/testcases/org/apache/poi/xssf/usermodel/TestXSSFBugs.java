/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.xssf.usermodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.openxml4j.opc.Package;
import org.openxml4j.opc.PackagePart;
import org.openxml4j.opc.PackagingURIHelper;

import junit.framework.TestCase;

public class TestXSSFBugs extends TestCase {
	private String getFilePath(String file) {
		File xml = new File(
				System.getProperty("HSSF.testdata.path") +
				File.separator + file
		);
		assertTrue(xml.exists());
		
		return xml.toString();
	}
	
	private Package saveAndOpen(XSSFWorkbook wb) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		wb.write(baos);
		ByteArrayInputStream inp = new ByteArrayInputStream(
				baos.toByteArray()
		);
		Package pkg = Package.open(inp);
		return pkg;
	}
	
	/**
	 * Named ranges had the right reference, but
	 *  the wrong sheet name
	 */
	public void test45430() throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(getFilePath("45430.xlsx"));
		assertEquals(3, wb.getNumberOfNames());
		
		assertEquals(0, wb.getNameAt(0).getCTName().getLocalSheetId());
		assertFalse(wb.getNameAt(0).getCTName().isSetLocalSheetId());
		assertEquals("SheetA!$A$1", wb.getNameAt(0).getReference());
		assertEquals("SheetA", wb.getNameAt(0).getSheetName());
		
		assertEquals(0, wb.getNameAt(1).getCTName().getLocalSheetId());
		assertFalse(wb.getNameAt(1).getCTName().isSetLocalSheetId());
		assertEquals("SheetB!$A$1", wb.getNameAt(1).getReference());
		assertEquals("SheetB", wb.getNameAt(1).getSheetName());
		
		assertEquals(0, wb.getNameAt(2).getCTName().getLocalSheetId());
		assertFalse(wb.getNameAt(2).getCTName().isSetLocalSheetId());
		assertEquals("SheetC!$A$1", wb.getNameAt(2).getReference());
		assertEquals("SheetC", wb.getNameAt(2).getSheetName());
	}
	
	/**
	 * We should carry vba macros over after save
	 */
	public void test45431() throws Exception {
		Package pkg = Package.open(getFilePath("45431.xlsm"));
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		
		PackagePart vba = pkg.getPart(
				PackagingURIHelper.createPartName("/xl/vbaProject.bin")
		);
		assertNotNull(vba);
		
		// Save and re-open, is still there
		Package nPkg = saveAndOpen(wb);
		XSSFWorkbook nwb = new XSSFWorkbook(nPkg);
		vba = nPkg.getPart(
				PackagingURIHelper.createPartName("/xl/vbaProject.bin")
		);
		assertNotNull(vba);
		
		// And again, just to be sure
		nPkg = saveAndOpen(nwb);
		nwb = new XSSFWorkbook(nPkg);
		vba = nPkg.getPart(
				PackagingURIHelper.createPartName("/xl/vbaProject.bin")
		);
		assertNotNull(vba);
		
		// For testing with excel
//		FileOutputStream fout = new FileOutputStream("/tmp/foo.xlsm");
//		nwb.write(fout);
//		fout.close();
	}
}