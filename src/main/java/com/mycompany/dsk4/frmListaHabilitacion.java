/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.dsk4;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.table.TableModel;
import java.util.Date; // Para la fecha
import java.text.SimpleDateFormat; // Para formatear la fecha
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Rectangle;


/**
 *
 * @author oscar
 */
public class frmListaHabilitacion extends javax.swing.JInternalFrame {
    private int fila;
    Object[] filas =new Object[11];
    javax.swing.table.DefaultTableModel modeloTabla=new javax.swing.table.DefaultTableModel();
   

    /**
     * Creates new form frmMantDepaEmpleados
     */
    public frmListaHabilitacion() {
        initComponents();
        configurarModelo();
    }
    private void configurarModelo() {
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Tipo de Material");
        modeloTabla.addColumn("Caras");
        modeloTabla.addColumn("Estilo");
        modeloTabla.addColumn("Tono");
        modeloTabla.addColumn("Cantos");
        modeloTabla.addColumn("Cantos Largos");
        modeloTabla.addColumn("Cantos Cortos");
        modeloTabla.addColumn("Largo");
        modeloTabla.addColumn("Ancho");
        modeloTabla.addColumn("Espesor");
        tablaDepartamentos.setModel(modeloTabla);     
    }
      
       private void leerDatos() {
         filas[0]=txtCantidad.getText();//
         filas[1]=txtDescripcion.getText();//
         filas[2]=cmbTipoMaterial.getSelectedItem().toString();//
         filas[3]=cmbCaras.getSelectedItem().toString();//
         filas[4]=cmbEstilo.getSelectedItem().toString();//
         filas[5]=cmbTono.getSelectedItem().toString();//
         filas[6]=txtCantos.getText();//
         filas[7]=cmbCantosLargos.getSelectedItem().toString();//
         filas[8]=cmbCantosCortos.getSelectedItem().toString();//
         filas[9]=txtLargo.getText();//
         filas[10]=txtAncho.getText();//        
         filas[11]=txtEspesor.getText();//     
    }
       
       private void guardarEnExcel() {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Lista_Habilitacion");
            TableModel model = tablaDepartamentos.getModel();

            try {
                // Escribir los encabezados de las columnas
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                }

                // Escribir los datos de la tabla
                for (int row = 0; row < model.getRowCount(); row++) {
                    Row dataRow = sheet.createRow(row + 1);
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        Cell cell = dataRow.createCell(col);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(""); // O algún valor por defecto para celdas vacías
                        }
                    }
                }

                // Ajustar el ancho de las columnas al contenido
                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Guardar el libro de Excel en un archivo
                try (FileOutputStream outputStream = new FileOutputStream("Lista_Habilitacion.xlsx")) {
                    workbook.write(outputStream);
                    JOptionPane.showMessageDialog(this, "La tabla se ha guardado en Lista_Habilitacion.xlsx", "Guardado en Excel", JOptionPane.INFORMATION_MESSAGE);
                }
                workbook.close();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo Excel", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error al guardar el archivo Excel: " + e.getMessage());
            }
}
    

    private void guardarEnPDF() {
           Document document = new Document();
           try {
               PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Lista_Habilitacion.pdf"));

               // Configurar encabezado y pie de página
               frmEmpleados.HeaderFooter event = new frmEmpleados.HeaderFooter();
               event.setHeader("Reporte de Departamentos"); // Encabezado general
               writer.setPageEvent(event);

               document.open();

               // Título principal
               Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
               Paragraph title = new Paragraph("Informe Departamentos", titleFont);
               title.setAlignment(Element.ALIGN_CENTER);
               document.add(title);

               // Subtítulo 1
               Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
               Paragraph subtitle1 = new Paragraph("Detalles de los Departamentos Registrados", subtitleFont);
               subtitle1.setAlignment(Element.ALIGN_LEFT);
               document.add(subtitle1);

               // Fecha
               SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
               Paragraph dateParagraph = new Paragraph("Fecha de Generación: " + dateFormat.format(new Date()),
                       FontFactory.getFont(FontFactory.HELVETICA, 10));
               dateParagraph.setAlignment(Element.ALIGN_RIGHT);
               document.add(dateParagraph);

               // Espacio en blanco antes de la tabla
               document.add(Chunk.NEWLINE);

               PdfPTable pdfTable = new PdfPTable(tablaDepartamentos.getColumnCount());
               pdfTable.setWidthPercentage(100);

               // Definir fuentes y colores
               Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
               Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
               BaseColor headerColor = new BaseColor(220, 220, 220);

               TableModel model = tablaDepartamentos.getModel();

               // Escribir los encabezados de las columnas
               for (int i = 0; i < model.getColumnCount(); i++) {
                   PdfPCell cell = new PdfPCell(new Phrase(model.getColumnName(i), headerFont));
                   cell.setBackgroundColor(headerColor);
                   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                   pdfTable.addCell(cell);
               }

               pdfTable.setHeaderRows(1);

               // Escribir los datos de la tabla
               for (int row = 0; row < model.getRowCount(); row++) {
                   for (int col = 0; col < model.getColumnCount(); col++) {
                       Object value = model.getValueAt(row, col);
                       PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", cellFont));
                       pdfTable.addCell(cell);
                   }
               }

               document.add(pdfTable);

               // Subtítulo 2 (después de la tabla)
               Paragraph subtitle2 = new Paragraph("Resumen de Datos", subtitleFont);
               subtitle2.setAlignment(Element.ALIGN_LEFT);
               document.add(Chunk.NEWLINE); // Espacio en blanco antes del subtítulo
               document.add(subtitle2);

               // Subtítulo 3 (ejemplo de contenido adicional)
               Paragraph subtitle3 = new Paragraph("Información Adicional", subtitleFont);
               subtitle3.setAlignment(Element.ALIGN_LEFT);
               document.add(Chunk.NEWLINE);
               document.add(subtitle3);

               document.close();
               JOptionPane.showMessageDialog(this, "La tabla se ha guardado en Departamentos.pdf", "Guardado en PDF",
                       JOptionPane.INFORMATION_MESSAGE);

           } catch (DocumentException e) {
               JOptionPane.showMessageDialog(this, "Error al crear el documento PDF", "Error", JOptionPane.ERROR_MESSAGE);
               e.printStackTrace();
           } catch (IOException e) {
               JOptionPane.showMessageDialog(this, "Error al guardar el archivo PDF", "Error", JOptionPane.ERROR_MESSAGE);
               e.printStackTrace();
           }
       }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbTipoMaterial = new javax.swing.JComboBox<>();
        cmbCantosCortos = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cmbEstilo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbTono = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtLargo = new javax.swing.JTextField();
        txtAncho = new javax.swing.JTextField();
        txtEspesor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCantos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbCaras = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cmbCantosLargos = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDepartamentos = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        btnPdf = new javax.swing.JButton();

        setTitle("Lista de Habilitacion");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Generales del Mueble"));

        jLabel1.setText("Descripción:");

        txtDescripcion.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setText("Tipo de Material:");

        jLabel3.setText("Caras:");

        cmbTipoMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mdf 25mm", "Mdf 16mm", "Mdf 12mm", "Mdf 9mm", "Mdf 6mm", "Mdf 3mm", "Panelar 16mm", "Triplay 16mm", "Acrilico 12mm", "Acrilico 9mm", "Acrilico 6mm", "Acrilico 3mm", "Tabla de 3/4", "Tablon de 1 1/2\"", "Perfil 4x1 1/2", "Perfil 3x3", "Perfil 3x1 1/2", "Perfil 2x2", "Perfil 2x1", "Perfil 2x1 1/2", "Perfil 1 1/2x1 1/2\"", "Perfil 1 1/2x1", "Perfil 1 1/2\"x1/2", "Perfil 1 1/2x3/4", "Perfil 1 1/4x1 1/4", "Perfil 1x1", "Perfil 1x1/2\"", "Perfil 1x3/4", "Perfil 3/4x3/4", "Perfil 7/8x7/8" }));

        cmbCantosCortos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        jLabel4.setText("Estilo:");

        cmbEstilo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Formaica", "Natural", "Melamina", "Metal", "Lamina" }));

        jLabel5.setText("Tono");

        cmbTono.setMaximumRowCount(20);
        cmbTono.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blanco", "Negro", "Chamoy", "Portland Maple", "Gris Folkstone", "Solar Oak", "Expresso Pear", "" }));

        jLabel6.setText("Largo:");

        jLabel7.setText("Ancho:");

        jLabel8.setText("Espesor:");

        txtLargo.setColumns(5);
        txtLargo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtAncho.setColumns(5);
        txtAncho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAncho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnchoActionPerformed(evt);
            }
        });

        txtEspesor.setColumns(5);
        txtEspesor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel9.setText("Cantidad:");

        txtCantidad.setColumns(5);
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.setToolTipText("");
        txtCantidad.setAlignmentX(15.0F);
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });

        jLabel10.setText("Cantos:");

        txtCantos.setColumns(5);
        txtCantos.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel11.setText("Cantos Largos:");

        cmbCaras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));
        cmbCaras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCarasActionPerformed(evt);
            }
        });

        jLabel12.setText("Cantos cortos:");

        cmbCantosLargos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtLargo)
                                    .addComponent(cmbEstilo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(162, 162, 162)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCantos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEspesor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(cmbTipoMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbCantosLargos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbCaras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbCantosCortos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTono, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDescripcion)
                    .addComponent(jLabel9)
                    .addComponent(txtCantidad)
                    .addComponent(jLabel11)
                    .addComponent(cmbCantosLargos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cmbCantosCortos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbEstilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbTono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(txtCantos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(cmbCaras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEspesor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Materiales"));

        tablaDepartamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"
            }
        ));
        jScrollPane1.setViewportView(tablaDepartamentos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnExcel.setText("Excel");
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        btnPdf.setText("PDF");
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEliminar)
                    .addComponent(btnCancelar)
                    .addComponent(btnModificar)
                    .addComponent(btnExcel)
                    .addComponent(btnPdf))
                .addGap(10, 10, 10))
        );

        jPanel2.getAccessibleContext().setAccessibleName("Lista de Departamento");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        modeloTabla.removeRow(fila);//elimina fila determinada
        tablaDepartamentos.setModel(modeloTabla);
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
            leerDatos();
        for (int i = 0; i < 6; i++) {//para la 6 columnas de la tabla
            modeloTabla.setValueAt(filas[i], fila, i);//Cambie en el modelo por lo que tiene almacenado el vector de filas
            tablaDepartamentos.setModel(modeloTabla);//Actualiza la tabla //(JTable) con el modelo
                        
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        leerDatos();
        modeloTabla.addRow(filas);
        tablaDepartamentos.setModel(modeloTabla);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        go:this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        // TODO add your handling code here:
        guardarEnExcel();
    }//GEN-LAST:event_btnExcelActionPerformed

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        // TODO add your handling code here:
        guardarEnPDF();
    }//GEN-LAST:event_btnPdfActionPerformed

    private void txtAnchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnchoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAnchoActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void cmbCarasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCarasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCarasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnPdf;
    private javax.swing.JComboBox<String> cmbCantosCortos;
    private javax.swing.JComboBox<String> cmbCantosLargos;
    private javax.swing.JComboBox<String> cmbCaras;
    private javax.swing.JComboBox<String> cmbEstilo;
    private javax.swing.JComboBox<String> cmbTipoMaterial;
    private javax.swing.JComboBox<String> cmbTono;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaDepartamentos;
    private javax.swing.JTextField txtAncho;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCantos;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtEspesor;
    private javax.swing.JTextField txtLargo;
    // End of variables declaration//GEN-END:variables

      


}

