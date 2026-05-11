/*
 * Copyright (C) 2026 IDRsolutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.idrsolutions.examples;

import org.jpedal.constants.PaperSize;
import org.jpedal.manipulator.PdfManipulator;

import javax.print.attribute.standard.PageRanges;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ManipulatePdf {

    public static void main(final String[] args) {

        // The PDF Manipulator class is used to manipulate and edit PDF files in bulk.
        // The class holds a queue of manipulations and can make changes with high throughput.

        // Create a new instance of the PDF Manipulator
        final PdfManipulator pdf = new PdfManipulator();

        // Select the folder we will load PDFs from
        final Path folder = Path.of("~/PDFs");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.pdf")) {

            // Iterate over each PDF file in the folder
            for (final Path path : stream) {
                try {
                    // Load our PDF file
                    pdf.loadDocument(path.toFile());

                    // Add a blank page at the beginning of the document
                    pdf.addPage(1, PaperSize.LEGAL_PORTRAIT);

                    // Flatten the layers in the file
                    pdf.flattenLayers();

                    // Perform a 2x2 imposition
                    pdf.nUp(2, 2);

                    // Rotate every page 90 degrees anti-clockwise
                    pdf.rotatePage(270);

                    // Apply the new changes
                    pdf.apply();

                    // Overwrite the file we loaded with the new version
                    pdf.writeDocument(path.toFile());
                } catch (final IOException e) {
                    // An IOException may be thrown if the manipulation fails
                    e.printStackTrace(System.err);
                } finally {
                    // Ensure the PDF Manipulator's queue is reset and the
                    // document is closed before loading the next file
                    pdf.reset();
                    pdf.closeDocument();
                }
            }

        } catch (final IOException e) {
            // An IOException may be thrown if the file reading fails
            e.printStackTrace(System.err);
        }

    }

}
