#pragma once

// This stuff is in source/device-list.c so I can't use them only with include to fitz

#define STACK_SIZE 96

enum fz_display_command
{
	FZ_CMD_BEGIN_PAGE,
	FZ_CMD_END_PAGE,
	FZ_CMD_FILL_PATH,
	FZ_CMD_STROKE_PATH,
	FZ_CMD_CLIP_PATH,
	FZ_CMD_CLIP_STROKE_PATH,
	FZ_CMD_FILL_TEXT,
	FZ_CMD_STROKE_TEXT,
	FZ_CMD_CLIP_TEXT,
	FZ_CMD_CLIP_STROKE_TEXT,
	FZ_CMD_IGNORE_TEXT,
	FZ_CMD_FILL_SHADE,
	FZ_CMD_FILL_IMAGE,
	FZ_CMD_FILL_IMAGE_MASK,
	FZ_CMD_CLIP_IMAGE_MASK,
	FZ_CMD_POP_CLIP,
	FZ_CMD_BEGIN_MASK,
	FZ_CMD_END_MASK,
	FZ_CMD_BEGIN_GROUP,
	FZ_CMD_END_GROUP,
	FZ_CMD_BEGIN_TILE,
	FZ_CMD_END_TILE
};

enum fz_display_node_flags { ISOLATED = 1, KNOCKOUT = 2 };

struct fz_display_node_s
{
	fz_display_command cmd;
	fz_display_node_s *next;
	fz_rect rect;
	union {
		fz_path *path;
		fz_text *text;
		fz_shade *shade;
		fz_image *image;
		int blendmode;
	} item;
	fz_stroke_state *stroke;
	int flag; /* even_odd, accumulate, isolated/knockout... */
	fz_matrix ctm;
	fz_colorspace *colorspace;
	float alpha;
	float color[FZ_MAX_COLORS];
};

struct fz_display_list_s
{
	fz_storable storable;
	fz_display_node_s *first;
	fz_display_node_s *last;
	int len;

	int top;
	struct {
		fz_rect *update;
		fz_rect rect;
	} stack[STACK_SIZE];
	int tiled;
};

