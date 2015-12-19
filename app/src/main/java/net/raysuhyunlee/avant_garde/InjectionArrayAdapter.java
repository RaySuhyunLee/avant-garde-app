package net.raysuhyunlee.avant_garde;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by RaySuhyunLee on 2015. 12. 11..
 */
public class InjectionArrayAdapter<T> extends ArrayAdapter<T> {
    public List<T> list;
    private final int resourceId;
    private LayoutInflater inflater;
    private final DrawViewInterface drawViewInterface;

    public InjectionArrayAdapter(Context context, int resourceId,
                                 List<T> list, DrawViewInterface drawViewInterface) {
        super(context, resourceId, list);
        this.list = list;
        this.resourceId = resourceId;
        this.drawViewInterface = drawViewInterface;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        return drawViewInterface.drawView(position, view, list.get(position));
    }

    public interface DrawViewInterface<T> {
        View drawView(int position, View view, T data);
    }
}