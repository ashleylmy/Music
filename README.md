# A gesture based music notation system

##  ShapeTrainer app trains the drawing and saves in database
    In main: switch to "Window.PANEL = new ShapeTrainerApp();" and train new gestures.

##  Create different layer for music notation system, Draw different Clefs and notes with stem, beam
    "E-W" start with a new Staff
    "E-W" add a new Staff to the current system
    "W-W" copy and add a new current system
    "S-S" draw bar line
    "SE-SW" draw F clef <br />
    "SW-SE" draw G clef <br />
    "SW-SW" add note head<br />
    "W-S"   add quarter rest<br />
    "E-S"   add 1/8 rest<br />
    "S-S"   stem or unstem head<br />
    "DOT"   augmentation dot<br />
    "S-N"   delete head<br />
    "E-E"   increment flags<br />
    "S-N"   decrease flag<br />
    "E-E"(cross two unbeamed stems)  create new beam<br />

##  Delete/Undo certain drawings