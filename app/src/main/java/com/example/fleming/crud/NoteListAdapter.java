package com.example.fleming.crud;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fleming on 3/12/16.
 */
public class NoteListAdapter extends ArrayAdapter<Note> {

    private List<Note> _noteList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public NoteListAdapter(Context context, int resource, List<Note> items) {
        super(context, resource, items);
        this.mSelectedItemsIds = new SparseBooleanArray();
        this._noteList = items;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.note_list_item, null, true);
        }

        TextView title = (TextView) convertView.findViewById(R.id.listItemTitle);
        TextView content = (TextView) convertView.findViewById(R.id.listItemContent);

        Note note = _noteList.get(position);

        title.setText(note.getTitle());
        content.setText(note.getContent());

        //put clickevent on ConvertView

        return convertView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if(value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedItemsIds() {
        return mSelectedItemsIds;
    }

    /**
     * Return the nodeList
     *
     * @return (List)
     */
    public List<Note> getNoteList() {
        return this._noteList;
    }

    /**
     * Return Note
     *
     * @param index
     * @return (Note)
     */
    public Note getNote(int index) {
        return this._noteList.get(index);
    }

}
