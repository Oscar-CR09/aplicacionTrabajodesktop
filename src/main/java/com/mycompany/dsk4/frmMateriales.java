/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.dsk4;


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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/**
 *
 * @author oscar
 */
public class frmMateriales extends javax.swing.JInternalFrame {
    private int fila;
    Object[] filas =new Object[14];
    javax.swing.table.DefaultTableModel modeloTabla=new javax.swing.table.DefaultTableModel();
    /**
     * Creates new form frmMantNomina
     */
    public frmMateriales() {
        initComponents();
        configurarModelo();      
    }
    
    
     private void configurarModelo() {
        modeloTabla.addColumn("Nombre de Material");
        modeloTabla.addColumn("Categoria de Material");
        modeloTabla.addColumn("Tipo de Material");
        modeloTabla.addColumn("Unidad de Medida");
        modeloTabla.addColumn("Costo Compra");
        modeloTabla.addColumn("Cantidad de Stock");
        modeloTabla.addColumn("Stock Minimo");
        modeloTabla.addColumn("Stock Maximo");
        modeloTabla.addColumn("Costo Unitario");
        modeloTabla.addColumn("Usuario de Registro");
        modeloTabla.addColumn("Estado del Material");
        modeloTabla.addColumn("Fecha Ultima Compra");
        modeloTabla.addColumn("Fecha Ultima Modificacion");
        modeloTabla.addColumn("Fecha Registro");
        modeloTabla.addColumn("Descripción");
        tablaMateriales.setModel(modeloTabla);    
    }
     private void detDatos() {
         
         filas[0]=txtNombreMaterial.getText();//
         filas[1]=txtCategoriaMaterial.getText();// 
         filas[2]=txtTipoMaterial.getText();//
         filas[3]=txtUnidadMedida.getText();//
         filas[4]=txtCostoCompra.getText();//
         filas[5]=txtCantidadStock.getText();//
         filas[6]=txtStockMinimo.getText();//
         filas[7]=txtStockMaximo.getText();//
         filas[8]=txtCostoUnitario.getText();//
         filas[9]=txtUsuarioRegistro.getText();//
         filas[10]=cmbEstadoMaterial.getSelectedItem().toString();//
         filas[11]=fechaUltimaCompra.getText();//
         filas[12]=fechaUltimaModificacion.getText();//
         filas[13]=fechaRegistro.getText();//
         filas[14]=txtDescripcion.getText();//
    }
       
      private void guardarEnExcel() {
        
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Empleados");
    TableModel model = tablaMateriales.getModel();

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
        try (FileOutputStream outputStream = new FileOutputStream("empleados.xlsx")) {
            workbook.write(outputStream);
            JOptionPane.showMessageDialog(this, "La tabla se ha guardado en empleados.xlsx", "Guardado en Excel", JOptionPane.INFORMATION_MESSAGE);
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
               PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("departamentos.pdf"));

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

               PdfPTable pdfTable = new PdfPTable(tablaMateriales.getColumnCount());
               pdfTable.setWidthPercentage(100);

               // Definir fuentes y colores
               Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
               Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
               BaseColor headerColor = new BaseColor(220, 220, 220);

               TableModel model = tablaMateriales.getModel();

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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombreMaterial = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCategoriaMaterial = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCantidadStock = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        txtStockMinimo = new javax.swing.JTextField();
        txtTipoMaterial = new javax.swing.JTextField();
        txtStockMaximo = new javax.swing.JTextField();
        txtUnidadMedida = new javax.swing.JTextField();
        txtCostoUnitario = new javax.swing.JTextField();
        txtCostoCompra = new javax.swing.JTextField();
        txtUsuarioRegistro = new javax.swing.JTextField();
        fechaRegistro = new datechooser.beans.DateChooserCombo();
        fechaUltimaCompra = new datechooser.beans.DateChooserCombo();
        fechaUltimaModificacion = new datechooser.beans.DateChooserCombo();
        cmbEstadoMaterial = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMateriales = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        btnPdf = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setTitle("Materiales");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Generales del Material"));

        jLabel1.setText("Nombre de Material:");

        jLabel2.setText("Categoria del Material:");

        txtCategoriaMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCategoriaMaterialActionPerformed(evt);
            }
        });

        jLabel3.setText("Cantidad deStock:");

        jLabel4.setText("Tipo de Material:");

        jLabel5.setText("Unidad de Medida:");

        jLabel6.setText("Stock Minimo:");

        jLabel7.setText("Stock Maximo:");

        jLabel8.setText("Costo Unitario:");

        jLabel9.setText("Costo Compra:");

        jLabel10.setText("Fecha Registro:");

        jLabel11.setText("Fecha Ultima Compra:");

        jLabel12.setText("Usuario registro:");

        jLabel13.setText("Fecha Ultima Modificacion:");

        jLabel14.setText("Estado Material:");

        jLabel15.setText("Descripción:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        cmbEstadoMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fechaUltimaModificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNombreMaterial)
                                    .addComponent(txtCategoriaMaterial)
                                    .addComponent(txtTipoMaterial)
                                    .addComponent(txtUnidadMedida)
                                    .addComponent(txtCostoCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(fechaUltimaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(txtUsuarioRegistro))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addGap(20, 20, 20)
                                    .addComponent(cmbEstadoMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel10))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtCantidadStock, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                            .addComponent(txtStockMinimo)
                                            .addComponent(txtStockMaximo)
                                            .addComponent(txtCostoUnitario))
                                        .addComponent(fechaRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 20, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNombreMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtCantidadStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCategoriaMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(txtTipoMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStockMaximo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)
                            .addComponent(txtUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCostoUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(jLabel10))
                            .addComponent(fechaRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtCostoCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12)
                        .addComponent(txtUsuarioRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fechaUltimaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jLabel14)
                        .addComponent(cmbEstadoMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fechaUltimaModificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabla de Materiales"));

        tablaMateriales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15"
            }
        ));
        jScrollPane1.setViewportView(tablaMateriales);

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");

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

        btnPdf.setText("pdf");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addComponent(btnPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(64, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)
                        .addGap(39, 39, 39)
                        .addComponent(btnExcel)
                        .addGap(18, 18, 18)
                        .addComponent(btnPdf))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        detDatos();
        modeloTabla.addRow(filas);
        tablaMateriales.setModel(modeloTabla);
   
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        go:this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtCategoriaMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCategoriaMaterialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCategoriaMaterialActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnPdf;
    private javax.swing.JComboBox<String> cmbEstadoMaterial;
    private datechooser.beans.DateChooserCombo fechaRegistro;
    private datechooser.beans.DateChooserCombo fechaUltimaCompra;
    private datechooser.beans.DateChooserCombo fechaUltimaModificacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaMateriales;
    private javax.swing.JTextField txtCantidadStock;
    private javax.swing.JTextField txtCategoriaMaterial;
    private javax.swing.JTextField txtCostoCompra;
    private javax.swing.JTextField txtCostoUnitario;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombreMaterial;
    private javax.swing.JTextField txtStockMaximo;
    private javax.swing.JTextField txtStockMinimo;
    private javax.swing.JTextField txtTipoMaterial;
    private javax.swing.JTextField txtUnidadMedida;
    private javax.swing.JTextField txtUsuarioRegistro;
    // End of variables declaration//GEN-END:variables

}
