package com.flowerroutine.v1tcc.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flowerroutine.v1tcc.R;
import com.flowerroutine.v1tcc.models.Procedimento;

import java.util.List;

public class AdapterProcedimentos extends BaseAdapter {

    private Context context;
    private List<Procedimento> procedimentos;

    public AdapterProcedimentos(Context context, List<Procedimento> procedimentos) {
        this.context = context;
        this.procedimentos = procedimentos;
    }


    @Override
    public int getCount() {
        return this.procedimentos.size();
    }

    @Override
    public Object getItem(int i) {
        return this.procedimentos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(this.context, R.layout.layout_procedimento, null);

        TextView txtNomeProcedimentoLayoutView = v.findViewById(R.id.txtNomeProcedimentoLayoutView);
        TextView txtHoraProcedimentoLayoutView = v.findViewById(R.id.txtHoraProcedimentoLayoutView);

        txtNomeProcedimentoLayoutView.setText(this.procedimentos.get(i).getNOME());
        txtHoraProcedimentoLayoutView.setText(this.procedimentos.get(i).getDATA_PREVISAO());

        return v;
    }
}
