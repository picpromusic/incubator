package inc;

import inc.myFirstActor.GetSomeNumber;
import inc.myFirstActor.PrintMe;
import inc.myFirstActor.ThrowsException;

public interface MyFirstActorInterface extends //
		PrintMe,//
		GetSomeNumber<Integer>,//
		ThrowsException,//
		ExternalLambda,//
		ConfigureLambda//
{}
