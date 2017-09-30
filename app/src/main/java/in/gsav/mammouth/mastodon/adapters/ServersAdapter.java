/*
 * This file is part of Mammouth.
 *
 * Mammouth is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mammouth is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package in.gsav.mammouth.mastodon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.gsav.mammouth.R;
import in.gsav.mammouth.mastodon.MastodonServer;

/**
 * Created by guilhelm.savin on 30/9/17.
 */

public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }

        public void setServer(MastodonServer server) {
            mTextView.setText(server.getName());
        }
    }

    private List<MastodonServer> servers;

    public ServersAdapter(List<MastodonServer> servers) {
        this.servers = servers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.server, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setServer(servers.get(position));
    }

    @Override
    public int getItemCount() {
        return servers.size();
    }
}
