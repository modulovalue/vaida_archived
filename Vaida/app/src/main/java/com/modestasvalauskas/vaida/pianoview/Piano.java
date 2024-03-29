package com.modestasvalauskas.vaida.pianoview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.modestasvalauskas.vaida.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Piano extends View {

    private static final int defaultKeyCount = 12;

    public TreeMap<Integer,Key> keymap_white;
    public TreeMap<Integer,Key> keymap_black;
    private TreeMap<Integer,Finger> fingers;
    private int white_key_resource_id;
    private int black_key_resource_id;
    public int key_count;
    public ArrayList<Integer> black_key_indexes = new ArrayList<Integer>(Arrays.asList(1,3,6,8,10));

    public Piano(Context context) {
        this(context, null);
    }

    public Piano(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Piano(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        int black_key_resource_id = R.drawable.key_black;
        int white_key_resource_id = R.drawable.key_white;
        int key_count = defaultKeyCount;

        if (attrs!=null) {
            // TypedArray a=getContext().obtainStyledAttributes(attrs, R.styleable.PianoView,0, 0);
            // black_key_resource_id = a.getResourceId(R.styleable.PianoView_blackKeyDrawable, R.drawable.key_black);
            // white_key_resource_id = a.getResourceId(R.styleable.PianoView_whiteKeyDrawable, R.drawable.key_white);
            // key_count = a.getInt(R.styleable.PianoView_keyCount, defaultKeyCount);
            // a.recycle();
        }

        initPiano(white_key_resource_id, black_key_resource_id, key_count);

    }

    public Piano(Context context, AttributeSet attrs, int defStyle, int keyCount) {
        super(context, attrs, defStyle);

        int black_key_resource_id = R.drawable.key_black;
        int white_key_resource_id = R.drawable.key_white;

        if (attrs!=null) {
            // TypedArray a=getContext().obtainStyledAttributes(attrs, R.styleable.PianoView,0, 0);
            // black_key_resource_id = a.getResourceId(R.styleable.PianoView_blackKeyDrawable, R.drawable.key_black);
            // white_key_resource_id = a.getResourceId(R.styleable.PianoView_whiteKeyDrawable, R.drawable.key_white);
            // key_count = a.getInt(R.styleable.PianoView_keyCount, defaultKeyCount);
            // a.recycle();
        }

        initPiano(white_key_resource_id, black_key_resource_id, keyCount);

    }

    private void initPiano(int white_key_resource_id, int black_key_resource_id, int key_count){
        this.key_count = key_count;
        this.fingers = new TreeMap<Integer, Finger>();
        this.white_key_resource_id = white_key_resource_id;
        this.black_key_resource_id = black_key_resource_id;
        this.keymap_white = new TreeMap<Integer, Key>();
        this.keymap_black = new TreeMap<Integer, Key>();
    
        for(int i = 0; i < key_count; i++){
            if(black_key_indexes.contains(i % 12)){
                keymap_black.put(i, new Key(i,this));
            } else {
                keymap_white.put(i, new Key(i,this));
            }   
        }
    }

    public void resetKeysToNotPressed() {
        for(int i = 0; i < key_count; i++){
            if(black_key_indexes.contains(i % 12)){
                keymap_black.get(i).reset();
            } else {
                keymap_white.get(i).reset();
            }
        }
    }

    public void setKeysToPressed(ArrayList<Integer> pressed) {
        resetKeysToNotPressed();
        for(int i = 0; i < key_count; i++){
            for(int j: pressed) {
                if(i == j) {
                    if(black_key_indexes.contains(i % 12)){
                        keymap_black.get(i).fingers.add(new Finger());
                    } else {
                        keymap_white.get(i).fingers.add(new Finger());
                    }
                }
            }

        }
    }

    public void setPianoKeyListener(PianoKeyListener listener){
        for(Key k : this.keymap_black.values()){
            k.setPianoKeyListener(listener);
        }
        for(Key k : this.keymap_white.values()){
            k.setPianoKeyListener(listener);
        }
    }

    public ArrayList<Integer> getPressedArray() {
        ArrayList<Integer> pressedArray = new ArrayList<>();


        for(int i = 0; i < key_count; i++){
            if(black_key_indexes.contains(i % 12)){
                if(keymap_black.get(i).isPressed()) {
                    pressedArray.add(i);
                }
            } else {
                if(keymap_white.get(i).isPressed()) {
                    pressedArray.add(i);
                }
            }
        }

        return pressedArray;
    }

    public interface PianoKeyListener{
        public void keyPressed(int id, int action);
    }

    private Drawable drawKey(Canvas canvas, Boolean pressed, int resource_id, int bounds_l, int bounds_t, int bounds_r, int bounds_b) throws Resources.NotFoundException, XmlPullParserException, IOException {
        Drawable key = (Drawable) Drawable.createFromXml(getResources(),getResources().getXml(resource_id));
        key.setState(pressed ? new int[] {android.R.attr.state_pressed} : new int[] {-android.R.attr.state_pressed});
        key.setBounds(bounds_l, bounds_t, bounds_r, bounds_b);
        key.draw(canvas);
        return key;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int KEY_WIDTH = this.getWidth()/this.keymap_white.size();
        int KEY_HEIGHT = this.getHeight();
        int counter = 0;

        try{

            for (Map.Entry<Integer,Key> key : this.keymap_white.entrySet()) {
                int bounds_left = counter * KEY_WIDTH;
                int bounds_top = 0;
                int bounds_right = (counter * KEY_WIDTH)+KEY_WIDTH;
                int bounds_bottom = KEY_HEIGHT;
                Drawable white_key = this.drawKey(canvas, key.getValue().isPressed(), white_key_resource_id, bounds_left, bounds_top, bounds_right, bounds_bottom);
                key.getValue().setDrawable(white_key);
                counter++;
            }

            counter = 0;

            for (Map.Entry<Integer,Key> key : keymap_black.entrySet()) {
                if( ((counter-2)%7 == 0) || ((counter-6)%7 == 0)) {
                    counter++;
                }

                int bounds_left = (counter * KEY_WIDTH)+(KEY_WIDTH/2);
                int bounds_top = 0;
                int bounds_right = (counter * KEY_WIDTH)+(KEY_WIDTH + (KEY_WIDTH/2));
                int bounds_bottom = KEY_HEIGHT/2;
                Drawable black_key = this.drawKey(canvas, key.getValue().isPressed(), black_key_resource_id, bounds_left, bounds_top, bounds_right, bounds_bottom);
                key.getValue().setDrawable(black_key);
                counter++;          
            }

            this.setOnTouchListener(new KeyPressListener(fingers,keymap_white,keymap_black));

        } catch (Exception e){
            Toast.makeText(this.getContext(), "Error drawing keys", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } 
    };



    private class KeyPressListener implements OnTouchListener{
        private TreeMap<Integer, Finger> fingers;
        private TreeMap<Integer, Key> keymap_white;
        private TreeMap<Integer, Key> keymap_black;

        public KeyPressListener(TreeMap<Integer, Finger> fingers, TreeMap<Integer, Key> keymap, TreeMap<Integer, Key> blackkeymap){
            this.fingers = fingers;
            this.keymap_white = keymap;
            this.keymap_black = blackkeymap;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int action = event.getActionMasked();

            switch (action) {
           // case MotionEvent.ACTION_MOVE: handleActionPointerMove(event); break;
            case MotionEvent.ACTION_DOWN: pushKeyDown(event); break;
            case MotionEvent.ACTION_UP: handleActionUp(event); break;
            case MotionEvent.ACTION_POINTER_DOWN: pushKeyDown(event); break;
            case MotionEvent.ACTION_POINTER_UP: handleActionUp(event); break;
            }

            return true;
        }

      //  public void handleActionPointerMove(MotionEvent event) {
      //      for(int i = 0; i < event.getPointerCount(); i++){
      //          handlePointerIndex(i, event);
      //      }
      //  }

        public void handleActionUp(MotionEvent event) {             
            int pointer_index = event.getPointerId(event.getActionIndex());
            if(fingers.containsKey(pointer_index)){
                fingers.get(pointer_index).lift();
                fingers.remove(pointer_index);
            } 
        }

        private void pushKeyDown(MotionEvent event){
            int pointer_index = event.getPointerId(event.getActionIndex());
            Key key = isPressingKey(event.getX(pointer_index), event.getY(pointer_index));
            if(!fingers.containsKey(pointer_index)){
                Finger finger = new Finger();
                finger.press(key);
                fingers.put(pointer_index,finger);
            } 
        }

     //   private void handlePointerIndex(int index, MotionEvent event){
     //       int pointer_id = event.getPointerId(index);
     //       //has it moved off a key?
     //       Key key = isPressingKey(event.getX(index), event.getY(index));
     //       Finger finger = fingers.get(pointer_id);
     //       if(key == null){
     //           finger.lift();
     //       } else if(finger.isPressing(key) == false){
     //           finger.lift();
     //           finger.press(key);
     //       }
     //   }

        private Key isPressingKey(float xpos,float ypos){
            Key pressing_key;
            if((pressing_key = isPressingKeyInSet(xpos, ypos, keymap_black)) != null){
                return pressing_key;
            } else {
                return isPressingKeyInSet(xpos, ypos, keymap_white);
            }
        }

        private Key isPressingKeyInSet(float xpos,float ypos, TreeMap<Integer,Key> keyset){
            for(Map.Entry<Integer,Key> entry : keyset.entrySet()){
                if(entry.getValue() != null && entry.getValue().getDrawable() != null && entry.getValue().getDrawable().getBounds().contains((int)xpos, (int)ypos)){
                    return entry.getValue();
                }
            }
            return null;
        }

    }

    public void shiftPianoBySemitone(int semitone) {
        ArrayList<Integer> afterShift = new ArrayList<>();
        for(Integer integer: getPressedArray()) {
            afterShift.add(integer + semitone);
        }
        setKeysToPressed(afterShift);
    }

    public static void shiftPianosBySemitone(List<Piano> pianos, int semitone) {
        for(Piano piano: pianos) {
            piano.shiftPianoBySemitone(semitone);
        }
    }

}

