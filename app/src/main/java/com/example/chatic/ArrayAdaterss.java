package com.example.chatic;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArrayAdaterss extends ArrayAdapter<AwesomeMessage> {// содержит обьекты нашей модели

   private List<AwesomeMessage>mesage;
   private Activity activity;

    public ArrayAdaterss(Activity context, int resource, List<AwesomeMessage> mesage) {// обавляем наш лист в конструктор
        super(context, resource,mesage);
        this.mesage=mesage;
        this.activity=context;


    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {// метод для создания элемента в листвиев

        //конвер вью это вью нашего элемента
        if (convertView==null){
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.list_view,parent,false);


            ImageView fotoImageView=convertView.findViewById(R.id.fotoImageViev);// записываем в переменные значения из лист виев
            TextView textView=convertView.findViewById(R.id.textView);
            TextView NameTextView=convertView.findViewById(R.id.nameTextView);
            AwesomeMessage model=getItem(position);// проводим по позициям наши запросы из модели

            boolean isText=model.getImageUrl()==null;// метод для изменения состояния элементов экрана
            if (isText){// если тру
                textView.setVisibility(View.VISIBLE);
                fotoImageView.setVisibility(View.GONE);
                textView.setText(model.getText());
            }else {
                textView.setVisibility(View.GONE);
                fotoImageView.setVisibility(View.VISIBLE);
                Glide.with(fotoImageView.getContext()).load(model.getImageUrl()).into(fotoImageView);// подключаем библиотеку глАЙД
                // вызываем из контекста фото и загружаем его в фотоимежю

            }
            NameTextView.setText(model.getName());// устанавливаем имя

        }
        return  convertView;
    }


            
        }


