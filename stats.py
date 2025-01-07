import os
import pandas as pd
import plotly.graph_objects as go
from plotly.subplots import make_subplots

# Check if file exists before trying to read
if not os.path.exists('lego_collections.csv'):
    print("You must first run the program to generate the CSV file` (lego_collections.csv)")
    exit(1)

df = pd.read_csv('lego_collections.csv')

fig1 = make_subplots(rows=2, cols=2, subplot_titles=("Sets by Theme", "Price vs Pieces", "Collection Status"))

# Sets by Theme
theme_counts = df['Theme'].value_counts()
fig1.add_trace(go.Bar(x=theme_counts.index, y=theme_counts.values, name="Sets by Theme"), row=1, col=1)

# Price vs Pieces
fig1.add_trace(go.Scatter(x=df['Pieces'], y=df['Price'], mode='markers', name="Price vs Pieces", 
                         text=df['Name'], hoverinfo='text+x+y'), row=1, col=2)

# Collection Status
status_counts = df['Status'].value_counts()
fig1.add_trace(go.Bar(x=status_counts.index, y=status_counts.values, name="Collection Status"), row=2, col=1)

fig1.update_layout(height=1080, width=1920, title_text="LEGO Collection Analysis")
fig1.show()
