// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
(LOOP)
  @SCREEN
  D=A
  @pantalla
  M=D  // direccion de pantalla

  @KBD
  D=M
  @BLACK
  D;JGT  // if(keyboard > 0) goto BLACK

  @color
  M=0  // color = blanco
  @PAINT
  0;JMP

(BLACK)
  @color
  M=-1  // color = negro

(PAINT)
  @color
  D=M
  @pantalla
  A=M
  M=D //Pantalla = color

  @pantalla
  M=M+1
  D=M

  @KBD
  D=A-D // Contador pantalla
  @PAINT
  D;JGT

@LOOP
0;JMP // infinite loop