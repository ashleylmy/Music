# A gesture based music notation system
class taught by Prof Marlin

##  ShapeTrainer app trains the drawing and saves in database
    In main: switch to "Window.PANEL = new ShapeTrainerApp();" and train new gestures.

##  Create different layer for music notation system, Draw different Clefs and notes with stem, beam
    "E-W" start with a new Staff
    "E-W" add a new Staff to the current system
    "W-W" copy and add a new current system
    "S-S" draw bar line
    "SE-SW" draw F clef
    "SW-SE" draw G clef
    "SW-SW" add note head
    "W-S"   add quarter rest
    "E-S"   add 1/8 rest
    "S-S"   stem or unstem head
    "DOT"   augmentation dot
    "S-N"   delete head
    "E-E"   increment flags
    "S-N"   decrease flag
    "E-E"(cross two unbeamed stems)  create new beam

##  Delete/Undo certain drawings