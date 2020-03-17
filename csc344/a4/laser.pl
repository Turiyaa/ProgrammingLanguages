
straight([X, Y, r], [NX, Y, r]) :- NX is X + 1.
straight([X, Y, u], [X, NY, u]) :- NY is Y + 1.
straight([X, Y, d], [X, NY, d]) :- NY is Y - 1.

deviate([X, Y, r], /, [X, NY, u]) :- NY is Y + 1.
deviate([X, Y, r], \, [X, NY, d]) :- NY is Y - 1.

deviate([X, Y, u], /, [NX, Y, r]) :- NX is X + 1.
deviate([X, Y, d], \, [NX, Y,r]) :- NX is X + 1.

nextMove(Position, Mirrors, NewPosition,  Mirrors) :- straight(Position, NewPosition).
nextMove([X, Y, D], Mirrors, NewPosition, NewMirror) :- deviate([X, Y, D],/,NewPosition), add_last([X,Y,/],Mirrors, NewMirror).
nextMove([X, Y, D], Mirrors, NewPosition, NewMirror) :-  deviate([X, Y, D],\,NewPosition), add_last([X,Y,/],Mirrors, NewMirror).


%% Safe Move.
safe([_, _, _],_, []).
	 
safe([X, Y, _],HumnaLocaltion, [Head|Obstacles]):-
	  rectangle([0, 0, 12, 10], X, Y),
	  \+rectangle(HumnaLocaltion, X, Y),
	 insideObstacle(Head,[CX, CY, CY1, CY2]),
	 \+rectangle([CX, CY, CY1, CY2], X, Y),
	 safe([X, Y, _],HumnaLocaltion, Obstacles).

%% Check if coordinate is inside gride
rectangle([X1,Y1,X2,Y2],X,Y):-
	 X>X1,
	 X=<X2,
         Y>Y1,
	 Y=<Y2.

insideObstacle([Blocks, Width, Height], [CX, CY, CY1, CY2]):-
         CX is Blocks,
         CY is 10-Height,
         CY1 is Blocks + Width,
         CY2 is 10.
% recurs :- nextMove(CurrentState, NewState), valid(NewState), recurs.
% recurs(Position, Position, _, _, ...)
% recurs(Position, Target, Obstacles, Mirrors, Path, FinalMirrors)
% place_mirrors(Y, O,Mirrors) :- recurs([1, Y, _], [12, Y, _], .., Mirrors)

%recurs(current, final, obstacles, mirror, path).
place_mirrors(Y, Obstacles, FinalMirrors) :-
	   recurs([1, Y, r], [12, Y, r],[2,0,4,6], Obstacles, [],[],FinalMirrors).

place_mirrors(Y, Obstacles, FinalMirrors) :-
	   recurs([1, Y, r], [12, Y, r],[3,0,5,6], Obstacles, [],[],FinalMirrors).

place_mirrors(Y, Obstacles, FinalMirrors) :-
	   recurs([1, Y, r], [12, Y, r],[4,0,6,6], Obstacles, [],[],FinalMirrors).

place_mirrors(Y, Obstacles, FinalMirrors) :-
	   recurs([1, Y, r], [12, Y, r],[5,0,7,6], Obstacles, [],[],FinalMirrors).

place_mirrors(Y, Obstacles, FinalMirrors) :-
	   recurs([1, Y, r], [12, Y, r],[6,0,8,6], Obstacles, [],[],FinalMirrors).

place_mirrors(Y, Obstacles, FinalMirrors) :-
	   recurs([1, Y, r], [12, Y, r],[7,0,9,6], Obstacles, [],[],FinalMirrors).

%Base Case
recurs(FinalState, FinalState,_,_,_,FinalMirrors,FinalMirrors).

%% check if position is in the path,
%% check x y not the direction

recurs(CurrentState, FinalState, HumnaLocaltion, Obstacles, Path, Mirrors,FinalMirrors) :-
	  nextMove(CurrentState, Mirrors, NewPosition, NewMirror),
	  safe(NewPosition,HumnaLocaltion,Obstacles),
          length(NewMirror, NumMirrors),
          NumMirrors =< 8,
	  \+memberchk(NewPosition, Path),
	  recurs(NewPosition, FinalState,HumnaLocaltion, Obstacles, [NewPosition|Path], NewMirror,FinalMirrors).

add_last(X,[],[X]).
add_last(X,[H|T],[H|TX]):-add_last(X,T,TX).

revert([], List, List).
revert([Head|Tail1],List, List3):-
          revert(Tail1, [Head|List], List3).

