package com.darylx.pdftest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create = (Button) findViewById(R.id.make_pdf_btn);
        Button delete = (Button) findViewById(R.id.read_pdf_btn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPDF(v);
                } catch (IOException e){
                    Log.e("ERROR",e.toString());
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPDF();
            }
        });
    }

    private void openPDF() {
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "temp.pdf");

        // The actual send
        Intent emailClient = new Intent(android.content.Intent.ACTION_SEND);
        emailClient.setType("plain/text");
        emailClient.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));//attachment
        startActivity(Intent.createChooser(emailClient, "Sending email..."));
    }

    private void createPDF(View v) throws IOException{
        // create PDF
        PdfDocument pdf =new PdfDocument();

        // create a page description
        PdfDocument.PageInfo pageInfo =new PdfDocument.PageInfo.Builder(612, 792, 0).create();

        Canvas canvas = null;

        // start page
        PdfDocument.Page page = pdf.startPage(pageInfo);

        // create bitmap from eahc byte[]
        BitmapFactory.Options options =new BitmapFactory.Options();
        options.inMutable =true;
        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);

        // matrix
        Matrix matrix =new Matrix();
        matrix.preTranslate(300, 300);
        matrix.preScale(2, 2);

        // add image to the page
        canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, matrix, null);

        // finish the page
        pdf.finishPage(page);

        // save PDF somehow...

        File copyFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "temp.pdf");
        OutputStream fileStream = new FileOutputStream(copyFile);
        pdf.writeTo(fileStream);

        // close PDF
        pdf.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
